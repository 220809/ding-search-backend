package com.dingzk.dingsearch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.mapper.PostMapper;
import com.dingzk.dingsearch.model.domain.Post;
import com.dingzk.dingsearch.service.PostService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author ding
* @since 2025-11-14 16:46:25
* 针对表【post(帖子)】的数据库操作Service实现
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

    @Resource
    private PostMapper postMapper;

    @Override
    public Page<Post> pageQueryPostByKeyword(String keyword, long page, long pageSize) {
        if (StringUtils.isBlank(keyword)) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "关键词为空");
        }

        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Post::getTitle, keyword).or()
                .like(Post::getTags, keyword).or()
                .like(Post::getContent, keyword);

        return postMapper.selectPage(Page.of(page, pageSize), queryWrapper);
    }
}




