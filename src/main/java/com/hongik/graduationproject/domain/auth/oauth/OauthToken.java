package com.hongik.graduationproject.domain.auth.oauth;

import lombok.Data;

@Data
public class OauthToken {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private int expiresTn;
    private String scope;
    private int refreshTokenExpiresIn;
}