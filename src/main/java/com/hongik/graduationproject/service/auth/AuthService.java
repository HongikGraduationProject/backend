package com.hongik.graduationproject.service.auth;

import com.hongik.graduationproject.domain.dto.auth.AuthRequest;
import com.hongik.graduationproject.domain.dto.auth.AuthResponse;
import com.hongik.graduationproject.domain.dto.auth.ReissueRequest;
import com.hongik.graduationproject.domain.dto.auth.ReissueResponse;

public interface AuthService {

    AuthResponse loginUser(AuthRequest authRequest);
    ReissueResponse reissueToken(ReissueRequest reissueRequest);
}