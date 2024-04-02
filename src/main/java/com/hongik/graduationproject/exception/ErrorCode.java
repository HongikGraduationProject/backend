package com.hongik.graduationproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_VIDEO_URL(HttpStatus.BAD_REQUEST, "유효하지 않은 영상 URL입니다");
    private final HttpStatus httpStatus;
    private final String message;
}
