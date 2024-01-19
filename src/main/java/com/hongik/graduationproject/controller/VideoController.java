package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.ApiResponse;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryDto;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateRequest;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
import com.hongik.graduationproject.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/summary/initiate")
    public ApiResponse<?> summarizeVideo(@RequestBody VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        log.info("summarize initiate video url={}", videoSummaryInitiateRequest.getUrl());
        return ApiResponse.createSuccess(videoService.sendUrlToQueue(videoSummaryInitiateRequest));
    }

    @GetMapping("/summary/status/{uuid}")
    public ApiResponse<?> hi(@PathVariable(name = "uuid") String uuid) {
        log.info("summarize status request uuid = {}", uuid);
        VideoSummaryDto videoSummaryDto = videoService.findByUuid(uuid);
        if (!(videoSummaryDto == null)) {
            log.info("summarize status response uuid = {}", videoSummaryDto.getUuid());
            return ApiResponse.createSuccess(videoSummaryDto);
        } else {
            log.info("summarize status response uuid = not found", uuid);
            return ApiResponse.createSuccessWithMessage("processing");
        }
    }
}
