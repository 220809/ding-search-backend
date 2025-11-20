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
import com.dingzk.dingsearch.mapper.UserMapper;
import com.dingzk.dingsearch.model.domain.User;
import com.dingzk.dingsearch.model.dto.UserEsDto;
import com.dingzk.dingsearch.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author dingzk
*  针对表【user(用户表)】的数据库操作Service实现
* @since 2025-11-14 10:45:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ElasticsearchClient esClient;

    @Override
    public Page<User> pageQueryUserByKeyword(String keyword, long page, long pageSize) {
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "关键词为空");
        }

        Query usernameQuery = MatchQuery.of(m -> m.field("username").query(keyword))._toQuery();
        Query tagQuery = TermQuery.of(t -> t.field("tags").value(keyword).caseInsensitive(true))._toQuery();
        Query deletedQuery = TermQuery.of(t -> t.field("deleted").value(0))._toQuery();

        List<Long> userIdList;
        SearchResponse<UserEsDto> searchResponse;
        try {
            searchResponse = esClient.search(s -> s
                            .index("user")
                            .query(q -> q
                                    .bool(b -> b
                                            .should(List.of(usernameQuery, tagQuery))
                                            .filter(List.of(deletedQuery))
                                            .minimumShouldMatch("1")
                                    ))
                            .size((int)(page * pageSize))
                    , UserEsDto.class);
            List<Hit<UserEsDto>> hits = searchResponse.hits().hits();
            if (CollectionUtils.isEmpty(hits)) {
                return Page.of(1, pageSize);
            }
            userIdList = hits.stream()
                    .map(h -> h.source().getId()).skip((page - 1) * pageSize)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "搜索出错了");
        }

        Page<User> userPage = new PageDTO<>(page, pageSize, searchResponse.hits().total().value());
        List<User> userList = userMapper.selectByIds(userIdList);
        userPage.setRecords(userList);
        return userPage;
    }

    private Page<User> pageQueryUserFromMySql(String keyword, long page, long pageSize) {
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "关键词为空");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUsername, keyword).or()
                .like(User::getTags, keyword);

        return userMapper.selectPage(Page.of(page, pageSize), queryWrapper);
    }
}




