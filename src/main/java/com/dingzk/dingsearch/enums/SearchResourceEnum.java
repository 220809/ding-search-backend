package com.dingzk.dingsearch.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 资源类型枚举类
 */
@RequiredArgsConstructor
@Getter
public enum SearchResourceEnum {

    POST("post"),
    USER("user")
    ;

    private final String type;
}
