package com.dingzk.dingsearch.controller;

import com.dingzk.dingsearch.common.ResponseEntity;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.domain.User;
import com.dingzk.dingsearch.model.vo.PostVo;
import com.dingzk.dingsearch.service.PostService;
import com.dingzk.dingsearch.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/query")
    public ResponseEntity<List<PostVo>> pageQueryPost(@RequestParam String keyword,
                                                      @RequestParam(defaultValue = "1") long page,
                                                      @RequestParam(defaultValue = "20")long pageSize) {
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException(ErrorCode.BAD_PARAMS);
        }
        List<Post> postList =
                postService.pageQueryPostByKeyword(keyword, page, pageSize);
        if (CollectionUtils.isEmpty(postList)) {
            return ResponseEntity.success(Collections.emptyList());
        }
        // 查询作者
        Set<Long> authodIdSet = postList.stream().map(Post::getAuthorId).filter(Objects::nonNull).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(authodIdSet)) {
            List<User> authorList = userService.listByIds(authodIdSet);
            Map<Long, User> idAuthorMap = authorList.stream().collect(Collectors.toMap(User::getId, user -> user));
            return ResponseEntity.success(PostVo.fromPostList(postList, idAuthorMap));
        }
        return ResponseEntity.success(PostVo.fromPostList(postList, Collections.emptyMap()));
    }
}
