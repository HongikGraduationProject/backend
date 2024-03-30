package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.*;
import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.VideoSummaryCategory;
import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import com.hongik.graduationproject.eum.Platform;
import com.hongik.graduationproject.repository.CategoryRepository;
import com.hongik.graduationproject.repository.VideoSummaryCategoryRepository;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
import com.hongik.graduationproject.repository.VideoSummaryStatusCacheRepository;
import com.hongik.graduationproject.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoSummaryService {
    private final MessageService messageService;
    private final VideoSummaryRepository videoSummaryRepository;
    private final VideoSummaryStatusCacheRepository videoSummaryStatusCacheRepository;
    private final CategoryRepository categoryRepository;
    private final VideoSummaryCategoryRepository videoSummaryCategoryRepository;

    public VideoSummaryInitiateResponse initiateSummarizing(VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        Platform platform = UrlUtils.getVideoPlatform(videoSummaryInitiateRequest.getUrl());
        String videoId = UrlUtils.getVideoId(videoSummaryInitiateRequest.getUrl(), platform);

        String videoCode = platform.toString().concat("_").concat(videoId);

        if (!videoSummaryStatusCacheRepository.existsById(videoCode)) {
            if (videoSummaryRepository.existsByVideoCode(videoCode)) {
                VideoSummary videoSummary = videoSummaryRepository.findByVideoCode(videoCode).get();

                videoSummaryStatusCacheRepository.save(VideoSummaryStatusCache.builder()
                        .videoCode(videoCode)
                        .videoSummaryId(videoSummary.getId())
                        .status("COMPLETE")
                        .generatedMainCategory(videoSummary.getGeneratedMainCategory())
                        .isCategoryIncluded(videoSummaryInitiateRequest.isCategoryIncluded())
                        .categoryId(videoSummaryInitiateRequest.getCategoryId())
                        .build());
            } else {
                messageService.sendVideoUrlToQueue(VideoSummaryInitiateMessage.builder()
                        .url(videoSummaryInitiateRequest.getUrl())
                        .platform(platform)
                        .videoCode(videoCode)
                        .build());

                videoSummaryStatusCacheRepository.save(VideoSummaryStatusCache.builder()
                        .videoCode(videoCode)
                        .videoSummaryId(-1L)
                        .status("PROCESSING")
                        .isCategoryIncluded(videoSummaryInitiateRequest.isCategoryIncluded())
                        .categoryId(videoSummaryInitiateRequest.getCategoryId())
                        .build());
            }
        }

        return new VideoSummaryInitiateResponse(videoCode);
    }

    public VideoSummaryDto getVideoSummaryById(Long videoSummaryId) {
        Optional<VideoSummary> videoSummary = videoSummaryRepository.findById(videoSummaryId);
        return VideoSummaryDto.from(videoSummary.get());
    }

    @Transactional
    public VideoSummaryStatusResponse getStatus(String videoCode) {
        VideoSummaryStatusCache statusCache = videoSummaryStatusCacheRepository.findById(videoCode).get();
        if (statusCache.getStatus().equals("COMPLETE")) {
            Category category = categoryRepository.findDefaultCategoryByUserIdAndMainCategory(1L, statusCache.getGeneratedMainCategory()).get();
            VideoSummary videoSummary = videoSummaryRepository.getReferenceById(statusCache.getVideoSummaryId());

            videoSummaryCategoryRepository.save(VideoSummaryCategory.builder()
                    .category(category)
                    .videoSummary(videoSummary)
                    .build());
        }
        return VideoSummaryStatusResponse.from(statusCache);
    }
}
