package com.dingzk.dingsearch.common;

import com.dingzk.dingsearch.exception.BusinessException;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ResponseEntity<T> implements Serializable {

    private final int code;

    private final T data;

    private final String message;

    public static <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(200, data, "ok");
    }

    public static ResponseEntity<?> error(int code, String message) {
        return new ResponseEntity<>(code, null, message);
    }

    public static ResponseEntity<?> error(BusinessException e) {
        return error(e.getCode(), e.getMessage());
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
