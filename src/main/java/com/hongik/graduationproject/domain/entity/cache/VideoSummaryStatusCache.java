package com.hongik.graduationproject.domain.entity.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "videoSummaryStatus", timeToLive = 60L)
public class VideoSummaryStatusCache {
    @Id
    private String videoCode;
    private Long videoSummaryId;
    private String status;
    private String generatedCategory;
    private String userSelectCategory;
    public void updateStatus(String status) {
        this.status = status;
    }

    public void updateVideoSummaryId(Long videoSummaryId) {
        this.videoSummaryId = videoSummaryId;
    }
}
