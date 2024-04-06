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
    VIDEO_SUMMARY_NOT_FOUND(HttpStatus.NOT_FOUND,"영상 요약 정보를 찾을 수 없습니다");
    private final HttpStatus httpStatus;
    private final String message;
}
