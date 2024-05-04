package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.video.*;
import com.hongik.graduationproject.service.VideoSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;

@Tag(name = "영상", description = "영상 또는 요약과 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class VideoSummaryController {
    private final VideoSummaryService videoSummaryService;

    @GetMapping("/test")
    public Response<String> test(@AuthenticationPrincipal Long principal) {
        System.out.println("principal = "+ principal);
        return Response.createSuccess("TEST");
    }

    @Operation(summary = "영상 요약 요청", description = "영상 요약 요청을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryInitiateResponse.class)))
    @PostMapping("/summaries/initiate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Response<VideoSummaryInitiateResponse> initiateSummarizing(@RequestBody VideoSummaryInitiateRequest videoSummaryInitiateRequest,
                                                                      @AuthenticationPrincipal Long userId) {
        log.info("summarize initiate video url={}", videoSummaryInitiateRequest.getUrl());
        return Response.createSuccess(videoSummaryService.initiateSummarizing(videoSummaryInitiateRequest, userId));
    }

    @Operation(summary = "영상 요약 상태", description = "영상 요약 상태 확인을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryStatusResponse.class)))
    @GetMapping("/summaries/status/{videoCode}")
    @ResponseStatus(HttpStatus.OK)
    public Response<VideoSummaryStatusResponse> getSummarizeStatus(@PathVariable(name = "videoCode")
                                                                   @Parameter(name = "videoCode", description = "영상 요약 요청에서 응답받은 비디오 코드", example = "INSTAGRAM_C4kWXhEuQpD")
                                                                   String videoCode,
                                                                   @AuthenticationPrincipal Long userId) {
        log.info("summarize status request videoCode = {}", videoCode);
        return Response.createSuccess(videoSummaryService.getStatus(videoCode, userId));
    }

    @Operation(summary = "영상 요약 조회", description = "영상 요약 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryDto.class)))
    @GetMapping("/summaries/{videoSummaryId}")
    @ResponseStatus(HttpStatus.OK)
    public Response<VideoSummaryDto> getSummaryByVideoSummaryId(@PathVariable(name = "videoSummaryId")
                                                                @Parameter(name = "videoSummaryId", description = "영상 요약 상태에서 응답받은 videoSummaryId", example = "3")
                                                                Long videoSummaryId) {
        log.info("summary requested videoSummaryId = {}", videoSummaryId);
        return Response.createSuccess(videoSummaryService.getVideoSummaryById(videoSummaryId));
    }

    @Operation(summary = "영상 요약 목록 조회", description = "categoryId로 영상 요약 목록 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryListResponse.class)))
    @GetMapping("/summaries")
    @ResponseStatus(HttpStatus.OK)
    public Response<VideoSummaryListResponse> getAllSummariesByCategoryId(@Parameter(required = true) @RequestParam Long categoryId) {
        return Response.createSuccess(videoSummaryService.getAllSummariesByCategoryId(categoryId));
    }

    @GetMapping("/hi")
    public Response<String> hi() {
        return Response.createSuccess("hi");
    }
}
