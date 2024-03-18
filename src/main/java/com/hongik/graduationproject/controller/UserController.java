package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.ApiResponse;
import com.hongik.graduationproject.domain.dto.KaKaoRequestDto;
import com.hongik.graduationproject.repository.TestEntityRepository;
import com.hongik.graduationproject.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final TestEntityRepository testEntityRepository;
    private final AuthService authService;

    @PostMapping("/oauth/token")
    public ApiResponse<?> getLogin(@RequestBody KaKaoRequestDto kaKaoRequestDto){
        return authService.loginUser(kaKaoRequestDto);
    }

    @GetMapping("/test")
    public ApiResponse<?> test(){
        return ApiResponse.createSuccess(testEntityRepository.findById(1L).get());
    }
}