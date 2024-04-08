package com.hongik.graduationproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_VIDEO_URL(HttpStatus.BAD_REQUEST, "유효하지 않은 영상 URL입니다."),
    ALREADY_REQUESTED_SUMMARIZING(HttpStatus.BAD_REQUEST, "사용자가 이미 해당 영상을 요약 요청했습니다. 잠시 후 다시 시도하세요"),
    FAILED_TO_EXTRACT_EXTRACT_ID(HttpStatus.BAD_REQUEST, "영상의 고유 ID를 추출하는데 실패했습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    FAILED_TO_RETRIEVE_USER_INFORMATION(HttpStatus.BAD_REQUEST, "사용자 정보를 가져오는데 실패했습니다."),
    USER_DUPLICATED(HttpStatus.CONFLICT, "이미 사용자가 존재합니다."),

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_VALIDATION_FAILED(HttpStatus.UNAUTHORIZED, "토큰 검증에 실패했습니다."),
    REFRESH_TOKEN_CREATE_FAILED(HttpStatus.UNAUTHORIZED, "Refresh Token 발급 실패");

    private final HttpStatus httpStatus;
    private final String message;
}
