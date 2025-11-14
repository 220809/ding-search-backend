package com.dingzk.dingsearch.model.request;

import com.dingzk.dingsearch.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class KeywordQueryRequest extends PageRequest implements Serializable {

    private String keyword;

    @Serial
    private static final long serialVersionUID = 1L;
}
