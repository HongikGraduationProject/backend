package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.auth.AuthResponse;
import com.hongik.graduationproject.domain.dto.auth.KaKaoRequest;
import com.hongik.graduationproject.service.auth.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/kakao/sign-up")
    public AuthResponse kakaoSignUp(@RequestBody KaKaoRequest kaKaoRequest){
        return kakaoAuthService.loginUser(kaKaoRequest);
    }
}