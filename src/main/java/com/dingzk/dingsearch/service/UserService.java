package com.dingzk.dingsearch.service;

import com.dingzk.dingsearch.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ding
* @since 2025-11-14 10:45:38
* 针对表【user(用户表)】的数据库操作Service
*/
public interface UserService extends IService<User> {
    List<User> pageQueryUserByKeyword(String keyword, long page, long pageSize);
}
