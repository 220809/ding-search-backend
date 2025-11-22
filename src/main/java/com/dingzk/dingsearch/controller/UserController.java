package com.dingzk.dingsearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.dingzk.dingsearch.common.ResponseEntity;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.model.domain.User;
import com.dingzk.dingsearch.model.request.UserQueryRequest;
import com.dingzk.dingsearch.model.vo.UserVo;
import com.dingzk.dingsearch.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping ("/query")
    public ResponseEntity<Page<UserVo>> pageQueryUser(@RequestBody UserQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.BAD_PARAMS);
        }
        Page<User> userPage =
                userService.pageQueryUser(request);
        Page<UserVo> userVoPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<User> userList = userPage.getRecords();
        if (CollectionUtils.isEmpty(userList)) {
            return ResponseEntity.success(userVoPage);
        }
        List<UserVo> userVoList = UserVo.fromUserList(userList);
        userVoPage.setRecords(userVoList);
        return ResponseEntity.success(userVoPage);
    }
}
