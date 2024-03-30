package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.*;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import com.hongik.graduationproject.eum.Platform;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
import com.hongik.graduationproject.repository.VideoSummaryStatusCacheRepository;
import com.hongik.graduationproject.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoSummaryService {
    private final MessageService messageService;
    private final VideoSummaryRepository videoSummaryRepository;
    private final VideoSummaryStatusCacheRepository videoSummaryStatusCacheRepository;

    public VideoSummaryInitiateResponse initiateSummarizing(VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        Platform platform = UrlUtils.getVideoPlatform(videoSummaryInitiateRequest.getUrl());
        String videoId = UrlUtils.getVideoId(videoSummaryInitiateRequest.getUrl(), platform);

        String videoCode = platform.toString().concat("_").concat(videoId);

        if (!videoSummaryStatusCacheRepository.existsById(videoCode)) {
            if (videoSummaryRepository.existsByVideoCode(videoCode)) {
                VideoSummary videoSummary = videoSummaryRepository.findByVideoCode(videoCode).get();
                videoSummaryStatusCacheRepository.save(new VideoSummaryStatusCache(videoCode, videoSummary.getId(), "COMPLETE", videoSummary.getGeneratedMainCategory(), null));
            } else {
                messageService.sendVideoUrlToQueue(VideoSummaryInitiateMessage.builder()
                        .url(videoSummaryInitiateRequest.getUrl())
                        .platform(platform)
                        .videoCode(videoCode)
                        .build());

                videoSummaryStatusCacheRepository.save(new VideoSummaryStatusCache(videoCode, -1L, "PROCESSING", null, null));
            }
        }

        return new VideoSummaryInitiateResponse(videoCode);
    }

    public VideoSummaryDto getVideoSummaryById(Long videoSummaryId) {
        Optional<VideoSummary> videoSummary = videoSummaryRepository.findById(videoSummaryId);
        return VideoSummaryDto.from(videoSummary.get());
    }

    public VideoSummaryStatusResponse getStatus(String videoCode) {
        VideoSummaryStatusCache statusCache = videoSummaryStatusCacheRepository.findById(videoCode).get();
        return VideoSummaryStatusResponse.from(statusCache);
    }
}
