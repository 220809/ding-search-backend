package com.dingzk.dingsearch.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    SYSTEM_ERROR(50000, "系统错误"),
    DATA_CONVERSION_ERROR(50001, "转换数据时出错"),
    BAD_PARAMS(40000, "请求参数错误"),
    ;


    private final int code;

    private final String message;
}
