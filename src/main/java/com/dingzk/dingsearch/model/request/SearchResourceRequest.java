package com.dingzk.dingsearch.model.request;

import com.dingzk.dingsearch.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 资源搜索请求参数包装类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchResourceRequest extends PageRequest implements Serializable {
    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 资源类型
     */
    private String type;

    @Serial
    private static final long serialVersionUID = 1L;
}
