package com.dingzk.dingsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.mapper.PostMapper;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.dto.PostEsDto;
import com.dingzk.dingsearch.service.PostService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
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

    @Override
    public Page<Post> pageQueryPostByKeyword(String keyword, long page, long pageSize) {
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "关键词为空");
        }

        // 构造查询条件
        Query titleQuery = MatchQuery.of(m -> m.field("title").query(keyword))._toQuery();
        Query contentQuery = MatchQuery.of(m -> m.field("content").query(keyword))._toQuery();
        Query tagQuery = TermQuery.of(t -> t.field("tags").value(keyword).caseInsensitive(true))._toQuery();
        Query deletedQuery = TermQuery.of(t -> t.field("deleted").value(0))._toQuery();

        List<Long> postIdList;
        SearchResponse<PostEsDto> searchResponse;
        try {
            searchResponse = esClient.search(s -> s
                            .index("post")
                            .query(q -> q
                                    .bool(b -> b
                                            .should(List.of(titleQuery, contentQuery, tagQuery))
                                            .filter(List.of(deletedQuery))
                                            .minimumShouldMatch("1")
                                    ))
                            .size((int)(page * pageSize))
                    , PostEsDto.class);
            List<Hit<PostEsDto>> hits = searchResponse.hits().hits();
            if (CollectionUtils.isEmpty(hits)) {
                return Page.of(1, pageSize);
            }
            postIdList = hits.stream()
                    .map(h -> h.source().getId()).skip((page - 1) * pageSize)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "搜索出错了");
        }

        Page<Post> postPage = new PageDTO<>(page, pageSize, searchResponse.hits().total().value());
        List<Post> postList = postMapper.selectByIds(postIdList);
        postPage.setRecords(postList);
        return postPage;
    }

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
}




