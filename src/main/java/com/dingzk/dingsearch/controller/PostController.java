package com.dingzk.dingsearch.controller;

import com.dingzk.dingsearch.common.ResponseEntity;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.vo.PostVo;
import com.dingzk.dingsearch.service.PostService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private PostService postService;

    @GetMapping("/query")
    public ResponseEntity<List<PostVo>> pageQueryPost(@RequestParam String keyword,
                                                      @RequestParam(defaultValue = "1") long page,
                                                      @RequestParam(defaultValue = "20")long pageSize) {
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException(ErrorCode.BAD_PARAMS);
        }
        List<Post> postList =
                postService.pageQueryPostByKeyword(keyword, page, pageSize);
        return ResponseEntity.success(PostVo.fromPostList(postList));
    }
}
