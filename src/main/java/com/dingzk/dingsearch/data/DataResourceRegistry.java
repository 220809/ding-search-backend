package com.dingzk.dingsearch.data;

import com.dingzk.dingsearch.data.impl.PostDataResource;
import com.dingzk.dingsearch.enums.SearchResourceEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据资源注册器类
 * 保存默认实现的 DataResource 或之后添加的 DataResource 实现类
 */
@Component
@Data
public class DataResourceRegistry {

    /**
     * 为演示两个实现方式，这里提供默认实现 PostDataResource
     * UserDataResource 实现第二种方式添加到 registry
     */
    @Resource
    private PostDataResource postDataResource;

    private final Map<String, DataResource<?>> dataResourceMap = new HashMap<>();

    @PostConstruct
    private void init() {
        dataResourceMap.put(SearchResourceEnum.POST.getType(), postDataResource);
    }

    public void addDataResource(DataResource<?> dataResource) {
        dataResourceMap.put(dataResource.getType(),  dataResource);
    }
}
