package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryDto;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateResponse;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryStatusResponse;
import com.hongik.graduationproject.service.VideoSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "영상", description = "영상 또는 요약과 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class VideoSummaryController {
    private final VideoSummaryService videoSummaryService;

    @Operation(summary = "영상 요약 요청", description = "영상 요약 요청을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryInitiateResponse.class)))
    @PostMapping("/summaries/initiate")
    public Response<VideoSummaryInitiateResponse> initiateSummarizing(@RequestBody VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        log.info("summarize initiate video url={}", videoSummaryInitiateRequest.getUrl());
        return Response.createSuccess(videoSummaryService.initiateSummarizing(videoSummaryInitiateRequest));
    }

    @Operation(summary = "영상 요약 상태", description = "영상 요약 상태 확인을 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryStatusResponse.class)))
    @GetMapping("/summaries/status/{videoCode}")
    public Response<VideoSummaryStatusResponse> getSummarizeStatus(@PathVariable(name = "videoCode")
                                                                       @Parameter(name = "videoCode", description = "영상 요약 요청에서 응답받은 비디오 코드", example = "INSTAGRAM_C4kWXhEuQpD")
                                                                       String videoCode) {
        log.info("summarize status request videoCode = {}", videoCode);
        return Response.createSuccess(videoSummaryService.getStatus(videoCode));
    }

    @Operation(summary = "영상 요약 조회", description = "영상 요약 조회를 위한 메소드")
    @ApiResponse(content = @Content(schema = @Schema(implementation = VideoSummaryDto.class)))
    @GetMapping("/summaries/{videoSummaryId}")
    public Response<VideoSummaryDto> getSummaryByVideoSummaryId(@PathVariable(name = "videoSummaryId")
                                                                    @Parameter(name = "videoSummaryId", description = "영상 요약 상태에서 응답받은 videoSummaryId", example = "3")
                                                                    Long videoSummaryId) {
        log.info("summary requested videoSummaryId = {}", videoSummaryId);
        return Response.createSuccess(videoSummaryService.getVideoSummaryById(videoSummaryId));
    }
}
