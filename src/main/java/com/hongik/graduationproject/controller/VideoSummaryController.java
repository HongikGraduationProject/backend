package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.ApiResponse;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateRequest;
import com.hongik.graduationproject.service.VideoSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class VideoSummaryController {
    private final VideoSummaryService videoSummaryService;

    @PostMapping("/summaries/initiate")
    public ApiResponse<?> summarizeVideo(@RequestBody VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        log.info("summarize initiate video url={}", videoSummaryInitiateRequest.getUrl());
        return ApiResponse.createSuccess(videoSummaryService.sendUrlToQueue(videoSummaryInitiateRequest));
    }

    @GetMapping("/summaries/status/{videoCode}")
    public ApiResponse<?> getSummarizeStatus(@PathVariable(name = "videoCode") String videoCode) {
        log.info("summarize status request videoCode = {}", videoCode);
        return ApiResponse.createSuccess(videoSummaryService.getStatus(videoCode));
    }

    @GetMapping("/summaries/{videoSummaryId}")
    public ApiResponse<?> getSummaryByVideoSummaryId(@PathVariable(name = "videoSummaryId") Long videoSummaryId) {
        log.info("summary requested videoSummaryId = {}", videoSummaryId);
        return ApiResponse.createSuccess(videoSummaryService.getVideoSummaryById(videoSummaryId));
    }
}
