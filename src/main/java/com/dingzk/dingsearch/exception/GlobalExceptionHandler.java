package com.dingzk.dingsearch.exception;

import com.dingzk.dingsearch.common.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException e) {
        log.info("Business exception occurred: {}", e.getMessage());
        return ResponseEntity.error(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.info("Exception occurred: {}", e.getMessage());
        return ResponseEntity.error(500, e.getMessage());
    }
}
