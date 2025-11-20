package com.dingzk.dingsearch.model.dto;

import com.dingzk.dingsearch.model.domain.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * Post 保存在 es 中的字段数据
 */
@Data
public class UserEsDto {

    private Long id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 性别（0-未设置，1-男，2-女）
     */
    private Integer gender;

    /**
     * 用户标签
     */
    private List<String> tags;

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
    private Integer deleted;

    private static final Gson GSON = new Gson();
    public static UserEsDto fromUser(User user) {
        UserEsDto userEsDto = new UserEsDto();
        BeanUtils.copyProperties(user, userEsDto);
        String userTags = user.getTags();
        List<String> tagList = GSON.fromJson(userTags, new TypeToken<List<String>>() {});
        userEsDto.setTags(tagList);
        return userEsDto;
    }
}
