package com.hongik.graduationproject.domain.entity.cache;

import com.hongik.graduationproject.eum.MainCategory;
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
    private MainCategory generatedMainCategory;
    private MainCategory userSelectMainCategory;

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
}