package com.dingzk.dingsearch.model.domain;

import lombok.Data;

/**
 * 用于测试 es client 的测试类
 */
@Data
public class Product {
    private String name;
    private String desc;
    private Double price;
}
