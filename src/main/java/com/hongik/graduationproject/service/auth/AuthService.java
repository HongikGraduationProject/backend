package com.hongik.graduationproject.service.auth;

import com.hongik.graduationproject.domain.dto.ApiResponse;
import com.hongik.graduationproject.domain.dto.AuthRequestDto;

public interface AuthService {
    ApiResponse<?> loginUser(AuthRequestDto authRequestDto);
}