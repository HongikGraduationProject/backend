package com.hongik.graduationproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_VIDEO_URL(HttpStatus.BAD_REQUEST, "유효하지 않은 영상 URL입니다."),
    FAILED_TO_EXTRACT_EXTRACT_ID(HttpStatus.BAD_REQUEST, "영상의 고유 ID를 추출하는데 실패했습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
