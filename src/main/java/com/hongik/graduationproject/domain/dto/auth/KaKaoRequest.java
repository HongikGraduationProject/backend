package com.hongik.graduationproject.domain.dto.auth;

import com.hongik.graduationproject.domain.dto.auth.AuthRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoRequest extends AuthRequest {

    String accessToken;
    String refreshToken;
}