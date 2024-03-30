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
        Long userId = videoSummaryInitiateRequest.getUserId();
        if (videoSummaryStatusCacheRepository.existsByVideoCodeAndUserId(videoCode, userId)) {
            // TODO: 해당 유저가 이미 요약하고 있다면 예외 발생
            System.out.println("예외발생");
            return new VideoSummaryInitiateResponse(videoCode);
        }

        // 이미 다른 사용자가 요약하고 있다면
        Optional<VideoSummaryStatusCache> statusCache = videoSummaryStatusCacheRepository.findFirstByVideoCode(videoCode);
        if (statusCache.isPresent()) {
            videoSummaryStatusCacheRepository.save(VideoSummaryStatusCache.clone(statusCache.get(), userId));
            return new VideoSummaryInitiateResponse(videoCode);
        }

        if (videoSummaryRepository.existsByVideoCode(videoCode)) {
            VideoSummary videoSummary = videoSummaryRepository.findByVideoCode(videoCode).get();

            videoSummaryStatusCacheRepository.save(VideoSummaryStatusCache.builder()
                    .videoCode(videoCode)
                    .videoSummaryId(videoSummary.getId())
                    .status("COMPLETE")
                    .userId(userId)
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
                    .userId(userId)
                    .isCategoryIncluded(videoSummaryInitiateRequest.isCategoryIncluded())
                    .categoryId(videoSummaryInitiateRequest.getCategoryId())
                    .build());
        }

        return new VideoSummaryInitiateResponse(videoCode);
    }

    public VideoSummaryDto getVideoSummaryById(Long videoSummaryId) {
        Optional<VideoSummary> videoSummary = videoSummaryRepository.findById(videoSummaryId);
        return VideoSummaryDto.from(videoSummary.get());
    }

    @Transactional
    public VideoSummaryStatusResponse getStatus(String videoCode) {
        VideoSummaryStatusCache statusCache = videoSummaryStatusCacheRepository.findByVideoCode(videoCode).get();
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
