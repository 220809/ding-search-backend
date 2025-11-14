package com.dingzk.dingsearch.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PageRequest implements Serializable {
    private int page = 1;
    private int pageSize = 20;
    @Serial
    private static final long serialVersionUID = 1L;
}
