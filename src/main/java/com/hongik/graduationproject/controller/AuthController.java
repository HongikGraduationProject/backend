package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.auth.ImeiJoinRequest;
import com.hongik.graduationproject.domain.dto.auth.ImeiJoinResponse;
import com.hongik.graduationproject.domain.dto.auth.ReissueRequest;
import com.hongik.graduationproject.domain.dto.auth.ReissueResponse;
import com.hongik.graduationproject.service.auth.AuthService;
import com.hongik.graduationproject.service.auth.ImeiAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final ImeiAuthService imeiAuthService;
    @PostMapping("/reissue")
    public  Response<ReissueResponse> reissueToken(@RequestBody ReissueRequest reissueRequest) {
        return Response.createSuccess(imeiAuthService.reissueToken(reissueRequest));
    }

    @PostMapping("")
    public Response<ImeiJoinResponse> joinUserWithImei(@RequestBody ImeiJoinRequest imeiJoinRequest) {
        return Response.createSuccess(imeiAuthService.joinUserWithImei(imeiJoinRequest));
    }
}
