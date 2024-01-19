package com.hongik.graduationproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private static final String SUCCESS_RESULT = "success";
    private static final String ERROR_RESULT = "error";

    private String result;
    private String message;
    private T data;

    public static <T> ApiResponse<T> createSuccess(T data) {
        return new ApiResponse<>(SUCCESS_RESULT, null, data);
    }

    public static ApiResponse<?> createSuccessWithNoData(String message) {
        return new ApiResponse<>(SUCCESS_RESULT, null, null);
    }

    public static ApiResponse<?> createSuccessWithMessage(String message) {
        return new ApiResponse<>(SUCCESS_RESULT, message, null);
    }

    public static ApiResponse<?> createError(String message) {
        return new ApiResponse<>(ERROR_RESULT, message, null);
    }
}
