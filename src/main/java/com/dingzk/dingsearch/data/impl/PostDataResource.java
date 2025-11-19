package com.dingzk.dingsearch.data.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.dingzk.dingsearch.data.DataResource;
import com.dingzk.dingsearch.enums.SearchResourceEnum;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.domain.User;
import com.dingzk.dingsearch.model.vo.PostVo;
import com.dingzk.dingsearch.service.PostService;
import com.dingzk.dingsearch.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PostDataResource implements DataResource<PostVo> {
    @Resource
    private PostService postService;

    @Resource
    private UserService userService;
    @Override
    public Page<PostVo> searchResource(String keyword, int page, int pageSize) {
        Page<Post> postPage =
                postService.pageQueryPostByKeyword(keyword, page, pageSize);
        List<Post> postList = postPage.getRecords();
        if (CollectionUtils.isEmpty(postList)) {
            return Page.of(1, pageSize);
        }
        // 查询作者
        Set<Long> authodIdSet = postList.stream().map(Post::getAuthorId).filter(Objects::nonNull).collect(Collectors.toSet());
        Page<PostVo> postVoPage = new PageDTO<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        if (!CollectionUtils.isEmpty(authodIdSet)) {
            List<User> authorList = userService.listByIds(authodIdSet);
            Map<Long, User> idAuthorMap = authorList.stream().collect(Collectors.toMap(User::getId, user -> user));
            List<PostVo> postVoList = PostVo.fromPostList(postList, idAuthorMap);
            postVoPage.setRecords(postVoList);
            return postVoPage;
        }
        List<PostVo> postVoList = PostVo.fromPostList(postList, Collections.emptyMap());
        postVoPage.setRecords(postVoList);
        return postVoPage;
    }

    @Override
    public String getType() {
        return SearchResourceEnum.POST.getType();
    }
}
