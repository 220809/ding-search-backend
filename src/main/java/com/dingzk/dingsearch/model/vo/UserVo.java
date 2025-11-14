package com.dingzk.dingsearch.model.vo;

import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.model.domain.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserVo implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别（0-未设置，1-男，2-女）
     */
    private Integer gender;

    /**
     * 用户头像
     */
    private String tags;

    /**
     * 角色（0-普通用户，1-管理员）
     */
    private Integer role;

    /**
     * 创建时间
     */
    private Date createTime;

    @Serial
    private static final long serialVersionUID = 1L;

    public static UserVo fromUser(User user) {
        UserVo result = new UserVo();
        try {
            BeanUtils.copyProperties(user, result);
        } catch (BeansException e) {
            throw new BusinessException(ErrorCode.DATA_CONVERSION_ERROR);
        }

        return result;
    }

    public static List<UserVo> fromUserList(List<User> userList) {
        if (userList.isEmpty()) {
            return Collections.emptyList();
        }
        return userList.stream().map(UserVo::fromUser).collect(Collectors.toList());
    }
}
