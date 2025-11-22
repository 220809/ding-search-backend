package com.dingzk.dingsearch.enums;

import com.dingzk.dingsearch.exception.BusinessException;
import com.dingzk.dingsearch.exception.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SearchOrderByEnum {

    CREATE_DESC("createTime_desc"),
    UPDATE_DESC("updateTime_desc");

    /**
     * 字段名_desc|asc
     * 如 createTime_desc 即按创建时间降序
     */
    private final String rule;

    public static SearchOrderByEnum toEnum(String rule) {
        for (SearchOrderByEnum e : SearchOrderByEnum.values()) {
            if (e.getRule().equals(rule)) {
                return e;
            }
        }
        throw new BusinessException(ErrorCode.BAD_PARAMS, "错误的排序参数");
    }
}
