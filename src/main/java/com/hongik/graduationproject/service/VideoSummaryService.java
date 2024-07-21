package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.*;
import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.VideoSummaryCategory;
import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import com.hongik.graduationproject.enums.Platform;
import com.hongik.graduationproject.exception.AppException;
import com.hongik.graduationproject.exception.ErrorCode;
import com.hongik.graduationproject.repository.*;
import com.hongik.graduationproject.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.hongik.graduationproject.enums.SummaryStatus.*;

@Service
@RequiredArgsConstructor
public class VideoSummaryService {
    private final MessageService messageService;
    private final VideoSummaryRepository videoSummaryRepository;
    private final VideoSummaryStatusCacheRepository summaryStatusCacheRepository;
    private final CategoryRepository categoryRepository;
    private final VideoSummaryCategoryRepository videoSummaryCategoryRepository;
    private final UserRepository userRepository;

    public VideoSummaryInitiateResponse initiateSummarizing(VideoSummaryInitiateRequest summaryInitiateRequest, Long userId) {
        Platform platform = UrlUtils.getVideoPlatform(summaryInitiateRequest.getUrl());
        String videoId = UrlUtils.getVideoId(summaryInitiateRequest.getUrl(), platform);

        String videoCode = platform.name() + '_' + videoId;

        if (checkDuplicateSummarizing(videoCode, userId)) {
            throw new AppException(ErrorCode.ALREADY_REQUESTED_SUMMARIZING);
        }

        Optional<VideoSummaryStatusCache> mayBeStatusCache = summaryStatusCacheRepository.findFirstByVideoCode(videoCode);
        if (mayBeStatusCache.isPresent()) {
            summaryStatusCacheRepository.save(VideoSummaryStatusCache.of(summaryInitiateRequest, userId, mayBeStatusCache.get()));
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

    private boolean checkDuplicateSummarizing(String videoCode, Long userId) {
        return videoSummaryCategoryRepository.existsByVideoCodeAndUserId(videoCode, userId) ||
                summaryStatusCacheRepository.existsByVideoCodeAndUserId(videoCode, userId);
    }

    // 무조건 중복허용이 안되는 로직
    public VideoSummaryDto getSummaryByVideoSummaryId(Long videoSummaryId, Long userId) {
        VideoSummary videoSummary = videoSummaryRepository.getReferenceById(videoSummaryId);
        User user = userRepository.getReferenceById(userId);
        VideoSummaryCategory videoSummaryCategory = videoSummaryCategoryRepository.findByVideoSummaryAndUser(videoSummary, user);

        return VideoSummaryDto.from(videoSummaryCategory);
    }

    @Transactional
    public VideoSummaryStatusResponse getStatus(String videoCode, Long userId) {
        VideoSummaryStatusCache statusCache = summaryStatusCacheRepository.findByVideoCodeAndUserId(videoCode, userId)
                .orElseThrow(() -> new AppException(ErrorCode.SUMMARIZING_STATUS_NOT_EXIST));

        if (statusCache.getStatus().equals(COMPLETE.name())) {
            Category category = categoryRepository.findDefaultCategoryByUserIdAndMainCategory(userId, statusCache.getGeneratedMainCategory())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXIST));
            VideoSummary videoSummary = videoSummaryRepository.getReferenceById(statusCache.getVideoSummaryId());

            videoSummaryCategoryRepository.save(VideoSummaryCategory.builder()
                    .category(category)
                    .videoSummary(videoSummary)
                    .build());

            summaryStatusCacheRepository.delete(statusCache);

        }
        return VideoSummaryStatusResponse.from(statusCache);
    }

    public VideoSummaryListResponse getAllSummariesByCategoryId(Long categoryId, Long userId) {
        Category category = categoryRepository.getReferenceById(categoryId);
        User user = userRepository.getReferenceById(userId);
        List<VideoSummaryResponse> videoSummaryResponseList = videoSummaryCategoryRepository.findAllByCategory(category,user).stream()
                .map(videoSummaryCategory -> new VideoSummaryResponse(videoSummaryCategory.getVideoSummary()))
                .toList();
        return new VideoSummaryListResponse(videoSummaryResponseList);
    }

    // 검색어를 포함하는 video id들을 조회하는 메서드
    public List<Long> getAllVideoIdsBySearchWord(String searchWord) {
        return videoSummaryRepository.getAllVideoIdsBySearchWord(searchWord);
    }

    @Transactional
    public void deleteVideoSummary(Long videoSummaryId) {
        VideoSummary videoSummary = videoSummaryRepository.findById(videoSummaryId)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_SUMMARY_NOT_FOUND));

        if (videoSummary.isDeleted()) {
            throw new AppException(ErrorCode.VIDEO_SUMMARY_ALREADY_DELETED);
        }

        videoSummary.markAsDeleted();
        videoSummaryRepository.save(videoSummary);
    }

    @Transactional
    public VideoSummaryListResponse getAllDeletedVideoSummary(){
        List<VideoSummaryResponse> deletedSummaryList = videoSummaryRepository.findAllByIsDeletedTrue()
                .stream()
                .map(VideoSummaryResponse::new)
                .toList();
        return new VideoSummaryListResponse(deletedSummaryList);
    }

    @Transactional
    public void restoreVideoSummary(Long videoSummaryId) {
        VideoSummary videoSummary = videoSummaryRepository.findDeletedById(videoSummaryId)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_SUMMARY_NOT_FOUND));

        videoSummary.restore();
        videoSummaryRepository.save(videoSummary);
    }
}