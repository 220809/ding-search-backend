package com.dingzk.dingsearch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.model.request.PostQueryRequest;

/**
* @author ding
* @since  2025-11-14 16:46:25
* 针对表【post(帖子)】的数据库操作Service
*/
public interface PostService extends IService<Post> {
    Page<Post> pageQueryPost(PostQueryRequest request);
}
