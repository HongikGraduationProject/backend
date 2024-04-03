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
    private final VideoSummaryStatusCacheRepository summaryStatusCacheRepository;
    private final CategoryRepository categoryRepository;
    private final VideoSummaryCategoryRepository videoSummaryCategoryRepository;

    public VideoSummaryInitiateResponse initiateSummarizing(VideoSummaryInitiateRequest summaryInitiateRequest) {
        Platform platform = UrlUtils.getVideoPlatform(summaryInitiateRequest.getUrl());
        String videoId = UrlUtils.getVideoId(summaryInitiateRequest.getUrl(), platform);

        String videoCode = platform.toString().concat("_").concat(videoId);

        Long userId = summaryInitiateRequest.getUserId();

        if (summaryStatusCacheRepository.existsByVideoCodeAndUserId(videoCode, userId)) {
            return new VideoSummaryInitiateResponse("해당 유저가 이미 요약 중인 영상입니다. 요약이 완료된 후 재요청 바랍니다.");
        }

        Optional<VideoSummaryStatusCache> statusCache = summaryStatusCacheRepository.findFirstByVideoCode(videoCode);
        if (statusCache.isPresent()) {
            summaryStatusCacheRepository.save(VideoSummaryStatusCache.of(summaryInitiateRequest, userId, statusCache.get()));
            return new VideoSummaryInitiateResponse(videoCode);
        }

        Optional<VideoSummary> mayBeVideoSummary = videoSummaryRepository.findByVideoCode(videoCode);
        if (mayBeVideoSummary.isPresent()) {
            VideoSummary videoSummary = mayBeVideoSummary.get();

            summaryStatusCacheRepository.save(VideoSummaryStatusCache.of(summaryInitiateRequest, userId, videoSummary));
            return new VideoSummaryInitiateResponse(videoCode);
        }

        messageService.sendVideoUrlToQueue(new VideoSummaryInitiateMessage(summaryInitiateRequest.getUrl(), videoCode, platform));

        summaryStatusCacheRepository.save(VideoSummaryStatusCache.of(summaryInitiateRequest, userId, videoCode));
        return new VideoSummaryInitiateResponse(videoCode);
    }

    private static VideoSummaryStatusCache of(VideoSummaryInitiateRequest summaryInitiateRequest, String videoCode, Long userId) {
        return VideoSummaryStatusCache.builder()
                .videoCode(videoCode)
                .videoSummaryId(-1L)
                .status("PROCESSING")
                .userId(userId)
                .isCategoryIncluded(summaryInitiateRequest.getIsCategoryIncluded())
                .categoryId(summaryInitiateRequest.getCategoryId())
                .build();
    }

    public VideoSummaryDto getVideoSummaryById(Long videoSummaryId) {
        Optional<VideoSummary> videoSummary = videoSummaryRepository.findById(videoSummaryId);
        return VideoSummaryDto.from(videoSummary.get());
    }

    @Transactional
    public VideoSummaryStatusResponse getStatus(String videoCode) {
        VideoSummaryStatusCache statusCache = summaryStatusCacheRepository.findByVideoCode(videoCode).get();
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
