package com.hongik.graduationproject.domain.entity.cache;

import com.hongik.graduationproject.eum.MainCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "videoSummaryStatus", timeToLive = 60L)
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

    public static VideoSummaryStatusCache clone(VideoSummaryStatusCache statusCache, Long userId) {
        return VideoSummaryStatusCache.builder()
                .videoCode(statusCache.getVideoCode())
                .userId(userId)
                .videoSummaryId(statusCache.getVideoSummaryId())
                .status(statusCache.getStatus())
                .generatedMainCategory(statusCache.getGeneratedMainCategory())
                .isCategoryIncluded(statusCache.getIsCategoryIncluded())
                .categoryId(statusCache.getCategoryId())
                .build();
    }

}