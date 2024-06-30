package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.auth.IssueRequest;
import com.hongik.graduationproject.domain.dto.auth.IssueTokenResponse;
import com.hongik.graduationproject.domain.dto.auth.ReissueRequest;
import com.hongik.graduationproject.domain.dto.auth.ReissueResponse;
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

    @Operation(summary = "토큰 발급 요청", description = "imei를 사용한 토큰 발급을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = IssueTokenResponse.class)))
    @PostMapping("")
    public Response<IssueTokenResponse> issueTokenByImei(@RequestBody IssueRequest issueRequest) {
        return Response.createSuccess(imeiAuthService.issueTokenFromImei(issueRequest));
    }
}
