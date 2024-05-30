package com.hongik.graduationproject.domain.dto.auth;

import com.nimbusds.oauth2.sdk.token.RefreshToken;

public record ImeiJoinResponse (
        String accessToken,
        String refreshToken
) {
}
