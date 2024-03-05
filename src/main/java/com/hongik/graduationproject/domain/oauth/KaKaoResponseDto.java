package com.hongik.graduationproject.domain.oauth;

import com.hongik.graduationproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaKaoResponseDto {

    String accessToken;
    String refreshToken;
    int exprTime;
    User user;
}