package com.hongik.graduationproject.domain.dto.video;

import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoSummaryStatusResponse {
    String status;
    Long videoSummaryId;
    public static VideoSummaryStatusResponse from(VideoSummaryStatusCache videoSummaryStatusCache) {
        return new VideoSummaryStatusResponse(videoSummaryStatusCache.getStatus(), videoSummaryStatusCache.getVideoSummaryId());
    }
}
