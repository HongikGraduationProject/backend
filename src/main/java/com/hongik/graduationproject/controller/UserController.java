package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.KaKaoRequestDto;
import com.hongik.graduationproject.service.auth.AuthService;
import com.hongik.graduationproject.domain.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final AuthService authService;

    @PostMapping("/oauth/token")
    public Response<?> getLogin(@RequestBody KaKaoRequestDto kaKaoRequestDto){
        return Response.createSuccess(authService.loginUser(kaKaoRequestDto));
    }

}