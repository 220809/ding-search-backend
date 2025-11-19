package com.dingzk.dingsearch.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 资源搜索结果
 * @author dingzi
 */
@Data
public class SearchResourceVo<T> implements Serializable {

    /**
     * 资源类型
     */
    private String resourceType;

    /**
     * 数据
     */
    private Page<T> data;

    @Serial
    private static final long serialVersionUID = 1L;

    public SearchResourceVo(String type, Page<T> dataPage) {
        this.resourceType = type;
        this.data = dataPage;
    }

    public static <T> SearchResourceVo<T> fromPage(String type, Page<T> dataPage) {
        return new SearchResourceVo<>(type, dataPage);
    }
}
