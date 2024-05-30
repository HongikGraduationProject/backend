package com.hongik.graduationproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_VIDEO_URL(HttpStatus.BAD_REQUEST, "유효하지 않은 영상 URL입니다."),
    ALREADY_REQUESTED_SUMMARIZING(HttpStatus.BAD_REQUEST, "사용자가 이미 해당 영상을 요약 요청했습니다."),
    FAILED_TO_EXTRACT_EXTRACT_ID(HttpStatus.BAD_REQUEST, "영상의 고유 ID를 추출하는데 실패했습니다."),
    VIDEO_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND,"영상 요약 정보를 찾을 수 없습니다"),
    MAIN_CATEGORY_NOT_EXISTS(HttpStatus.BAD_REQUEST,"해당 메인 카테고리가 존재하지 않습니다."),
    SUMMARIZING_STATUS_NOT_EXIST(HttpStatus.NOT_FOUND, "요약 정보를 찾을 수 없습니다."),
    CATEGORY_NOT_EXIST(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입한 사용자입니다."),
    JWT_NOT_EXISTS(HttpStatus.BAD_REQUEST, "요청에 JWT가 존재하지 않습니다."),
    INVALID_JWT(HttpStatus.BAD_REQUEST, "유효하지 않은 JWT입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
