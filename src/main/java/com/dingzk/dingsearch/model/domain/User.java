package com.dingzk.dingsearch.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 用户表
 */
@TableName(value ="user")
@Data
@Builder
public class User {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
     * 密码
     */
    private String password;

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

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    private Integer deleted;
}