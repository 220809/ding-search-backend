package com.dingzk.dingsearch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dingzk.dingsearch.model.domain.User;
import com.dingzk.dingsearch.model.request.UserQueryRequest;

/**
* @author ding
* @since 2025-11-14 10:45:38
* 针对表【user(用户表)】的数据库操作Service
*/
public interface UserService extends IService<User> {
    Page<User> pageQueryUser(UserQueryRequest request);
}
