package com.dingzk.dingsearch.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    SYSTEM_ERROR(50000, "系统错误"),
    ;


    private final int code;

    private final String message;
}
