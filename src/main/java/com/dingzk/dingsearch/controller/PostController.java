package com.dingzk.dingsearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.dingzk.dingsearch.common.ResponseEntity;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.domain.User;
import com.dingzk.dingsearch.model.request.PostQueryRequest;
import com.dingzk.dingsearch.model.vo.PostVo;
import com.dingzk.dingsearch.service.PostService;
import com.dingzk.dingsearch.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @PostMapping("/query")
    public ResponseEntity<Page<PostVo>> pageQueryPost(@RequestBody PostQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.BAD_PARAMS);
        }
        Page<Post> postPage =
                postService.pageQueryPost(request);
        List<Post> postList = postPage.getRecords();
        if (CollectionUtils.isEmpty(postList)) {
            return ResponseEntity.success(Page.of(request.getPage(), request.getPageSize()));
        }
        // 查询作者
        Set<Long> authodIdSet = postList.stream().map(Post::getAuthorId).filter(Objects::nonNull).collect(Collectors.toSet());
        Page<PostVo> postVoPage = new PageDTO<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        if (!CollectionUtils.isEmpty(authodIdSet)) {
            List<User> authorList = userService.listByIds(authodIdSet);
            Map<Long, User> idAuthorMap = authorList.stream().collect(Collectors.toMap(User::getId, user -> user));
            List<PostVo> postVoList = PostVo.fromPostList(postList, idAuthorMap);
            postVoPage.setRecords(postVoList);
            return ResponseEntity.success(postVoPage);
        }
        List<PostVo> postVoList = PostVo.fromPostList(postList, Collections.emptyMap());
        postVoPage.setRecords(postVoList);
        return ResponseEntity.success(postVoPage);
    }
}
