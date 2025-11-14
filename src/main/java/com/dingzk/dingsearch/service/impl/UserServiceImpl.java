package com.dingzk.dingsearch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingzk.dingsearch.model.domain.User;
import com.dingzk.dingsearch.mapper.UserMapper;
import com.dingzk.dingsearch.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
* @author dingzk
*  针对表【user(用户表)】的数据库操作Service实现
* @since 2025-11-14 10:45:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> pageQueryUserByKeyword(String keyword, long page, long pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUsername, keyword).or()
                .like(User::getTags, keyword);

        Page<User> userPage = userMapper.selectPage(Page.of(page, pageSize), queryWrapper);
        if (CollectionUtils.isEmpty(userPage.getRecords())) {
            return Collections.emptyList();
        }

        return userPage.getRecords();
    }
}




