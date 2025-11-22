package com.dingzk.dingsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingzk.dingsearch.enums.SearchOrderByEnum;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.mapper.PostMapper;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.dto.PostEsDto;
import com.dingzk.dingsearch.model.request.PostQueryRequest;
import com.dingzk.dingsearch.service.PostService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author ding
* @since 2025-11-14 16:46:25
* 针对表【post(帖子)】的数据库操作Service实现
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

    @Resource
    private PostMapper postMapper;

    @Resource
    private ElasticsearchClient esClient;

    private Page<Post> pageQueryPostFromMySql(String keyword, long page, long pageSize) {
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "关键词为空");
        }

        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Post::getTitle, keyword).or()
                .like(Post::getTags, keyword).or()
                .like(Post::getContent, keyword);

        return postMapper.selectPage(Page.of(page, pageSize), queryWrapper);
    }

    @Override
    public Page<Post> pageQueryPost(PostQueryRequest request) {
        // 提取查询参数
        String title = request.getTitle();
        String keyword = request.getKeyword();
        Long authorId = request.getAuthorId();
        List<String> andTagList = request.getAndTagList();
        List<String> orTagList = request.getOrTagList();
        List<String> orderByList = request.getOrderByList();
        int page = request.getPage();
        int pageSize = request.getPageSize();

        // 校验
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "关键词为空");
        }

        // 默认根据分数排序
        List<SortOptions> sortOptionsList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderByList)) {
            for (String orderBy : orderByList) {
                SearchOrderByEnum orderByEnum = SearchOrderByEnum.toEnum(orderBy);
                SortOptions sortOptions = null;
                switch (orderByEnum) {
                    case CREATE_DESC ->
                            sortOptions = SortOptions.of(s -> s.field(f -> f.field("createTime").order(SortOrder.Desc)));
                    case UPDATE_DESC ->
                            sortOptions = SortOptions.of(s -> s.field(f -> f.field("updateTime").order(SortOrder.Desc)));
                }
                if (sortOptions != null) {
                    sortOptionsList.add(sortOptions);
                }
            }
        }
        sortOptionsList.add(
                SortOptions.of(s -> s.score(sc -> sc.order(SortOrder.Desc)))
        );

        // 构造 es 查询条件
        List<Query> shouldQueryList = new ArrayList<>();
        // 标题查询
        if (StringUtils.isNotBlank(title)) {
            shouldQueryList.add(
                    MatchQuery.of(m -> m.field("title").query(title))._toQuery()
            );
        }

        // 关键词查询: 匹配文章标题与内容
        shouldQueryList.add(
                MatchQuery.of(m -> m.field("title").query(keyword))._toQuery()
        );
        shouldQueryList.add(
                MatchQuery.of(m -> m.field("content").query(keyword))._toQuery()
        );

        // 过滤查询
        List<Query> filterQueryList = new ArrayList<>();
        // 查询指定作者
        if (authorId != null && authorId > 0) {
            filterQueryList.add(
                    TermQuery.of(t -> t.field("authorId").value(authorId))._toQuery()
            );
        }
        // 查询指定满足全部标签
        if (CollectionUtils.isNotEmpty(andTagList)) {
            for (String tag : andTagList) {
                filterQueryList.add(
                        TermQuery.of(t -> t.field("tags").value(tag).caseInsensitive(true))._toQuery()
                );
            }
        }

        // 查询满足任一标签的
        if (CollectionUtils.isNotEmpty(orTagList)) {
            for (String tag : orTagList) {
                shouldQueryList.add(
                        TermQuery.of(t -> t.field("tags").value(tag).caseInsensitive(true))._toQuery()
                );
            }
        }

        SearchResponse<PostEsDto> searchResponse;
        // es 查询
        try {
            searchResponse = esClient.search(s -> s
                            .index("post")
                            .query(q -> q
                                    .bool(b -> b
                                            .should(shouldQueryList)
                                            .filter(filterQueryList)
                                            .minimumShouldMatch("1")
                                    ))
                            .sort(sortOptionsList)
                            .from(page - 1)
                            .size(pageSize)
                    , PostEsDto.class);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "搜索出错了");
        }

        if (searchResponse == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "搜索出错了");
        }
        List<Hit<PostEsDto>> hits = searchResponse.hits().hits();
        if (CollectionUtils.isEmpty(hits)) {
            return Page.of(page, pageSize);
        }
        List<Long> postIdList = hits.stream()
                .filter(hit -> hit.source() != null)
                .map(hit -> hit.source().getId())
                .toList();
        // 查询其他未存入 es 的数据（保存在数据库中）
        // 按序返回

        Page<Post> postPage = new PageDTO<>(page, pageSize, searchResponse.hits().total().value());
        List<Post> postList = postMapper.selectByIds(postIdList);
        Map<Long, List<Post>> idPostMap = postList.stream().collect(Collectors.groupingBy(Post::getId));

        List<Post> resultList = new ArrayList<>();
        for (Long postId : postIdList) {
            resultList.add(idPostMap.get(postId).get(0));
        }
        postPage.setRecords(resultList);

        return postPage;
    }
}




