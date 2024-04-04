package com.hongik.graduationproject.domain.dto.auth.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthToken {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private int expiresTn;
    private String scope;
    private int refreshTokenExpiresIn;
}