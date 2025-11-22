package com.dingzk.dingsearch.data;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingzk.dingsearch.enums.SearchResourceEnum;
import com.dingzk.dingsearch.model.request.SearchResourceRequest;

/**
 * 搜索资源接口
 * <p>定义搜索资源接入规范
 *
 * <p>新增搜索资源类型方式:
 * 首先，新增的数据资源类型需要实现此接口，并在 {@link SearchResourceEnum} 中添加相应资源类型枚举
 * 接下来，你需要将新创建的 DataResource 注册到 {@link DataResourceRegistry} 中，你有两种方式可以实现：
 * <p>
 * 1. 在允许修改 DataResourceRegistry 代码的前提下，你可以直接在其代码中将新增的资源类型添加到 map 中。
 * <p>
 * 2. 实现 {@link DataResourceConfigurer} 为配置类，可参考 {@link UserDataResourceConfig}，然后可将新增的资源类型添加到注册器
 */
public interface DataResource<T> {

    /**
     * 资源搜索逻辑
     * @param request request
     * @return 资源
     */
    Page<T> searchResource(SearchResourceRequest request);

    String getType();
}
