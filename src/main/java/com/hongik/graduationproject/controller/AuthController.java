package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.auth.ImeiJoinRequest;
import com.hongik.graduationproject.domain.dto.auth.ImeiJoinResponse;
import com.hongik.graduationproject.domain.dto.auth.ReissueRequest;
import com.hongik.graduationproject.domain.dto.auth.ReissueResponse;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateResponse;
import com.hongik.graduationproject.service.auth.AuthService;
import com.hongik.graduationproject.service.auth.ImeiAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증, 인가", description = "인증 인가와 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final ImeiAuthService imeiAuthService;

    @Operation(summary = "reissue 요청", description = "reissue를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ReissueRequest.class)))
    @PostMapping("/reissue")
    public  Response<ReissueResponse> reissueToken(@RequestBody ReissueRequest reissueRequest) {
        return Response.createSuccess(imeiAuthService.reissueToken(reissueRequest));
    }


    @Operation(summary = "회원가입 요청", description = "imei를 사용한 회원가입을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = ImeiJoinRequest.class)))
    @PostMapping("")
    public Response<ImeiJoinResponse> joinUserWithImei(@RequestBody ImeiJoinRequest imeiJoinRequest) {
        return Response.createSuccess(imeiAuthService.joinUserWithImei(imeiJoinRequest));
    }
}
