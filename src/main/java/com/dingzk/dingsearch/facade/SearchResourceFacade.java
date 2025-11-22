package com.dingzk.dingsearch.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingzk.dingsearch.data.DataResource;
import com.dingzk.dingsearch.data.DataResourceRegistry;
import com.dingzk.dingsearch.enums.SearchResourceEnum;
import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import com.dingzk.dingsearch.model.request.SearchResourceRequest;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SearchResourceFacade {

    @Resource
    private DataResourceRegistry dataResourceRegistry;

    public Page<?> searchResource(SearchResourceRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "请求参数为空");
        }
        String resourceType = request.getType();
        if (StringUtils.isBlank(resourceType)) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "资源类型为空");
        }
        boolean validEnumValue = Arrays.stream(SearchResourceEnum.values()).anyMatch(e -> e.getType().equals(resourceType));
        if (!validEnumValue) {
            throw new BusinessException(ErrorCode.BAD_PARAMS, "未知的资源类型");
        }
        DataResource<?> dataResource = dataResourceRegistry.getDataResourceMap().get(resourceType);

        return dataResource.searchResource(request);
    }
}
