package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.KaKaoRequest;
import com.hongik.graduationproject.domain.dto.ReissueRequest;
import com.hongik.graduationproject.service.auth.AuthService;
import com.hongik.graduationproject.domain.dto.auth.KaKaoRequest;
import com.hongik.graduationproject.service.auth.KakaoAuthService;
import com.hongik.graduationproject.domain.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/kakao/sign-up")
    public Response<?> kakaoSignUp(@RequestBody KaKaoRequest kaKaoRequest){
        return kakaoAuthService.loginUser(kaKaoRequest);
    }

    @PostMapping("/auth/kakao/reissue")
    public Response<?> reissueToken(@RequestBody KaKaoRequestDto kaKaoRequestDto){
        return authService.reissueToken(kaKaoRequestDto);
    }
}