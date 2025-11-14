package com.dingzk.dingsearch.service;

import com.dingzk.dingsearch.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testSaveUser() {
        User user = User.builder()
                .account("user_01")
                .build();
        boolean result = userService.save(user);
        Assertions.assertTrue(result);
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void testUpdateById() {
        final long userIdToUpdate = 1L;
        final String updatedUsername = "hello1";
        final int updatedGender = 2;

        User user = userService.getById(userIdToUpdate);
        User updateUser = User.builder()
                .id(userIdToUpdate)
                .username(updatedUsername)
                .gender(updatedGender)
                .build();
        user.setUsername(updatedUsername);
        user.setGender(updatedGender);
        user.setUpdateTime(null);
        boolean result = userService.updateById(updateUser);
        Assertions.assertTrue(result);
        User updatedUser = userService.getById(userIdToUpdate);
        // 清除更新时间影响
        updatedUser.setUpdateTime(null);
        Assertions.assertEquals(user, updatedUser);
    }

    @Test
    void testRemoveById() {
        User user = User.builder()
                .account("delete_user")
                .build();
        boolean result = userService.save(user);
        Assertions.assertTrue(result);
        User savedUser = userService.getById(user.getId());
        Assertions.assertNotNull(savedUser);
        boolean result1 = userService.removeById(user.getId());
        Assertions.assertTrue(result1);
        Assertions.assertNull(userService.getById(user.getId()));
    }

    @Test
    void testQueryUserByKeyword() {
        final String queryKeyword = "test";
        List<User> users = userService.pageQueryUserByKeyword(queryKeyword, 1, 20);
        Assertions.assertEquals(2, users.size());
    }
}