package com.hongik.graduationproject.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReissueResponse {

    String accessToken;
    String refreshToken;
    int exprTime;
}