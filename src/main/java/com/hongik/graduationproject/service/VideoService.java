package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryStatusResponse;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryDto;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateResponse;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final MessageService messageService;
    private final VideoSummaryRepository videoSummaryRepository;

    public VideoSummaryInitiateResponse sendUrlToQueue(VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        String uuid = UUID.randomUUID().toString();
        videoSummaryInitiateRequest.setUuid(uuid);
        messageService.sendVideoUrlToQueue(videoSummaryInitiateRequest);
        return new VideoSummaryInitiateResponse(uuid);
    }

    public VideoSummaryDto getVideoByUuid(String uuid) {
        Optional<VideoSummary> videoSummary = videoSummaryRepository.findByUuid(uuid);
        return VideoSummaryDto.from(videoSummary.get());
    }

    public VideoSummaryStatusResponse getStatus(String uuid) {
        return new VideoSummaryStatusResponse(videoSummaryRepository.existsByUuid(uuid) ? "COMPLETE" : "PROCESSING");
    }
}
