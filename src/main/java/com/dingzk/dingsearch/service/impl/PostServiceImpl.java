package com.dingzk.dingsearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.service.PostService;
import com.dingzk.dingsearch.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
* @author ding
* @since 2025-11-14 16:46:25
* 针对表【post(帖子)】的数据库操作Service实现
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

}




