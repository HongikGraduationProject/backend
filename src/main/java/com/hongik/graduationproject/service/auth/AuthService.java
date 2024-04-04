package com.hongik.graduationproject.service.auth;

import com.hongik.graduationproject.domain.dto.KaKaoRequest;
import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.AuthRequest;

public interface AuthService {

    Response<?> loginUser(AuthRequest authRequest);
    Response<?> reissueToken(KaKaoRequest kaKaoRequest);
}