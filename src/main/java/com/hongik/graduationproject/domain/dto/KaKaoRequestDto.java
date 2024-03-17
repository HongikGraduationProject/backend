package com.hongik.graduationproject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaKaoRequestDto extends AuthRequestDto {

    String accessToken;
    String refreshToken;
}