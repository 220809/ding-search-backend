package com.dingzk.dingsearch.service;

import com.dingzk.dingsearch.model.domain.Post;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ding
* @since  2025-11-14 16:46:25
* 针对表【post(帖子)】的数据库操作Service
*/
public interface PostService extends IService<Post> {
    List<Post> pageQueryPostByKeyword(String keyword, long page, long pageSize);
}
