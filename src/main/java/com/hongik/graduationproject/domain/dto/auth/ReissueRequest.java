package com.hongik.graduationproject.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReissueRequest {

    private String accessToken;
    private String refreshToken;
}