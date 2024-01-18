package com.hongik.graduationproject.controller;

import com.hongik.graduationproject.domain.dto.ApiResponse;
import com.hongik.graduationproject.domain.dto.video.VideoSummarizeRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummarizeResponse;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
import com.hongik.graduationproject.service.VideoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VideoController {
    private final VideoService videoService;
    private final VideoSummaryRepository videoSummaryRepository;
    @PostMapping("/summary")
    public ApiResponse<?> summarizeVideo(@RequestBody VideoSummarizeRequest videoSummarizeRequest) {
        log.info("summarize request video url={}", videoSummarizeRequest.getUrl());
        return ApiResponse.createSuccess(videoService.sendUrlToQueue(videoSummarizeRequest));
    }

    @GetMapping("/test")
    public String hi(){
        System.out.println(videoSummaryRepository.findByUuid("dsfas").getUuid());
        return "hi";
    }
}
