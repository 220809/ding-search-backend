package com.dingzk.dingsearch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.request.PostQueryRequest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

    @Resource
    private PostService postService;

    @Test
    void pageQueryPostByKeyword() {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        final String queryKeyword = "Java";
        postQueryRequest.setKeyword(queryKeyword);
        Page<Post> postList = postService.pageQueryPost(postQueryRequest);
        Assertions.assertTrue(!postList.getRecords().isEmpty());
    }
}