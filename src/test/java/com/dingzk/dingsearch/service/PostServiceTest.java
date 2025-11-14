package com.dingzk.dingsearch.service;

import com.dingzk.dingsearch.model.domain.Post;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PostServiceTest {

    @Resource
    private PostService postService;

    @Test
    void pageQueryPostByKeyword() {
        final String queryKeyword = "Java";
        List<Post> postList = postService.pageQueryPostByKeyword(queryKeyword, 1, 20);
        Assertions.assertTrue(!postList.isEmpty());
    }
}