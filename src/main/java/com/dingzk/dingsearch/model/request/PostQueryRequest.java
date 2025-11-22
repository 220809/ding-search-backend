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
public class PostQueryRequest extends PageRequest implements Serializable {
    /**
     * 标题
     */
    private String title;

    /**
     * 关键词
     */
    private String keyword;

    /**
     * 作者id
     */
    private Long authorId;

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

    public static PostQueryRequest fromSearchRequest(SearchResourceRequest request) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        BeanUtils.copyProperties(request, postQueryRequest);
        return postQueryRequest;
    }
}
