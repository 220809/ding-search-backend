package com.dingzk.dingsearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingzk.dingsearch.model.domain.User;
import com.dingzk.dingsearch.mapper.UserMapper;
import com.dingzk.dingsearch.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author dingzk
*  针对表【user(用户表)】的数据库操作Service实现
* @since 2025-11-14 10:45:38
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




