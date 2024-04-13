package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.auth.ReissueRequest;
import com.hongik.graduationproject.domain.dto.auth.ReissueResponse;
import com.hongik.graduationproject.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService AuthService;

    @PostMapping("/reissue")
    public  ReissueResponse reissueToken(@RequestBody ReissueRequest reissueRequest) {
        return AuthService.reissueToken(reissueRequest);
    }
}
