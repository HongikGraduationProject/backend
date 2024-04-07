package com.hongik.graduationproject.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String result,
        String message
) {
    public static ErrorResponse of (ErrorCode errorCode) {
        return ErrorResponse.builder()
                .result("error")
                .message(errorCode.getMessage())
                .build();
    }
}
