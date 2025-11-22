package com.dingzk.dingsearch.model.request;

import com.dingzk.dingsearch.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * 用户名
     */
    private String username;

    /**
     * 关键词
     */
    private String keyword;

    private Integer gender;

    /**
     * 角色（0-普通用户，1-管理员）
     */
    private Integer role;

    /**
     * 满足全部标签条件
     */
    private List<String> andTagList;

    /**
     * 满足任一标签条件
     */
    private List<String> orTagList;

    /**
     * 排序规则
     */
    private List<String> orderByList;

    @Serial
    private static final long serialVersionUID = 1L;

    public static UserQueryRequest fromSearchRequest(SearchResourceRequest request) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        BeanUtils.copyProperties(request, userQueryRequest);
        return userQueryRequest;
    }
}
