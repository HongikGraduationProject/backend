package com.hongik.graduationproject.service.auth;

import com.hongik.graduationproject.domain.dto.KaKaoRequestDto;
import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.AuthRequestDto;

public interface AuthService {

    Response<?> loginUser(AuthRequestDto authRequestDto);
    Response<?> reissueToken(KaKaoRequestDto kaKaoRequestDto);
}