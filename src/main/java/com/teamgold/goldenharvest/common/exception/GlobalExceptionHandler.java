package com.teamgold.goldenharvest.common.exception;

import com.teamgold.goldenharvest.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("BusinessException: {}: {}", errorCode.getCode(), errorCode.getMessage(), e);
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.fail(errorCode.getCode(), errorCode.getMessage()));
    }
}
