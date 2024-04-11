package com.hongik.graduationproject.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AppException.class)
    ResponseEntity<ErrorResponse> AppExceptionHandler(AppException ex, HttpServletRequest request) {
        log.error("AppException 발생: {}", ex.getErrorCode().getMessage());
        log.error("에러가 발생한 지점 {}, {}", request.getMethod(), request.getRequestURI());
        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(ErrorResponse.of(ex.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleConflict(MethodArgumentTypeMismatchException ex) {
        log.error("ConversionFailedException: {}", ex.getMessage());
        return ResponseEntity
                .status(ErrorCode.MAIN_CATEGORY_NOT_EXISTS.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.MAIN_CATEGORY_NOT_EXISTS));
    }

}
