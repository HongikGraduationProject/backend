package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.ApiResponse;
import com.hongik.graduationproject.domain.oauth.KaKaoRequestDto;
import com.hongik.graduationproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/oauth/token")
    public ApiResponse<?> getLogin(@RequestBody KaKaoRequestDto kaKaoRequestDto){
        return ApiResponse.createSuccess(userService.saveUser(kaKaoRequestDto));
    }
}