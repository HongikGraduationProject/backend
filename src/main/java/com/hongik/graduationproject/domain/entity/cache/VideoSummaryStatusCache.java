package com.hongik.graduationproject.domain.entity.cache;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateRequest;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.enums.MainCategory;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "videoSummaryStatus")
public class VideoSummaryStatusCache {
    @Id
    private String id;
    @Indexed
    private String videoCode;
    @Indexed
    private Long userId;
    private Long videoSummaryId;
    private String status;
    private MainCategory generatedMainCategory;
    private Boolean isCategoryIncluded;
    private Long categoryId;

    public void updateStatus(String status) {
        this.status = status;
    }

    public void updateVideoSummaryId(Long videoSummaryId) {
        this.videoSummaryId = videoSummaryId;
    }

    public void updateGeneratedMainCategory(MainCategory mainCategory) {
        if (this.generatedMainCategory == null) {
            this.generatedMainCategory = mainCategory;
        }
    }

    public static VideoSummaryStatusCache of(VideoSummaryInitiateRequest summaryInitiateRequest, Long userId, VideoSummaryStatusCache statusCache) {
        return VideoSummaryStatusCache.builder()
                .videoCode(statusCache.getVideoCode())
                .userId(userId)
                .videoSummaryId(statusCache.getVideoSummaryId())
                .status(statusCache.getStatus())
                .generatedMainCategory(statusCache.getGeneratedMainCategory())
                .isCategoryIncluded(summaryInitiateRequest.getIsCategoryIncluded())
                .categoryId(summaryInitiateRequest.getCategoryId())
                .build();
    }

    public static VideoSummaryStatusCache of(VideoSummaryInitiateRequest summaryInitiateRequest, Long userId, VideoSummary videoSummary) {
        return VideoSummaryStatusCache.builder()
                .videoCode(videoSummary.getVideoCode())
                .videoSummaryId(videoSummary.getId())
                .status("COMPLETE")
                .userId(userId)
                .generatedMainCategory(videoSummary.getGeneratedMainCategory())
                .isCategoryIncluded(summaryInitiateRequest.getIsCategoryIncluded())
                .categoryId(summaryInitiateRequest.getCategoryId())
                .build();
    }

    public static VideoSummaryStatusCache of(VideoSummaryInitiateRequest summaryInitiateRequest, Long userId, String videoCode) {
        return VideoSummaryStatusCache.builder()
                .videoCode(videoCode)
                .videoSummaryId(-1L)
                .status("PROCESSING")
                .userId(userId)
                .isCategoryIncluded(summaryInitiateRequest.getIsCategoryIncluded())
                .categoryId(summaryInitiateRequest.getCategoryId())
                .build();
    }

}