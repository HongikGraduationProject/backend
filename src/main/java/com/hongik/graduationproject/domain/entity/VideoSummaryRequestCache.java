package com.hongik.graduationproject.domain.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "videoSummaryRequest")
public class VideoSummaryRequestCache {
    @Id
    private String videoCode;
    private Long id;
    private String status;

    public VideoSummaryRequestCache(String videoCode) {
        this.videoCode = videoCode;
        this.id = null;
        this.status = "PROCESSING";
    }
}
