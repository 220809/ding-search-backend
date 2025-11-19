package com.dingzk.dingsearch.data.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.dingzk.dingsearch.data.DataResource;
import com.dingzk.dingsearch.enums.SearchResourceEnum;
import com.dingzk.dingsearch.model.domain.User;
import com.dingzk.dingsearch.model.vo.UserVo;
import com.dingzk.dingsearch.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class UserDataResource implements DataResource<UserVo> {
    @Resource
    private UserService userService;
    @Override
    public Page<UserVo> searchResource(String keyword, int page, int pageSize) {
        Page<User> userPage =
                userService.pageQueryUserByKeyword(keyword, page, pageSize);
        Page<UserVo> userVoPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<User> userList = userPage.getRecords();
        if (CollectionUtils.isEmpty(userList)) {
            return Page.of(1, pageSize);
        }
        List<UserVo> userVoList = UserVo.fromUserList(userList);
        userVoPage.setRecords(userVoList);
        return userVoPage;
    }

    @Override
    public String getType() {
        return SearchResourceEnum.USER.getType();
    }
}
