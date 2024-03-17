package com.hongik.graduationproject.domain.entity.cache;

import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "videoSummaryStatus")
public class VideoSummaryStatusCache {
    @Id
    @Indexed
    private String videoCode;
    private Long videoSummaryId;
    private String status;

    public VideoSummaryStatusCache(String videoCode) {
        this.videoCode = videoCode;
        this.videoSummaryId = -1L;
        this.status = "PROCESSING";
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void updateVideoSummaryId(Long videoSummaryId) {
        this.videoSummaryId = videoSummaryId;
    }
}
