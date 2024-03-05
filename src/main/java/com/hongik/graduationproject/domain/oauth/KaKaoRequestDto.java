package com.hongik.graduationproject.domain.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoRequestDto {

    String accessToken;
    String refreshToken;
}