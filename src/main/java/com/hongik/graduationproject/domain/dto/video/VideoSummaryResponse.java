package com.hongik.graduationproject.domain.dto.video;

import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.eum.Platform;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record VideoSummaryResponse(
        String title,
        String description,
        List<String> keywords,
        String url,
        String summary,
        String address,
        LocalDateTime createdAt,
        Platform platform
) {
    public VideoSummaryResponse(VideoSummary videoSummary) {
        this(
                videoSummary.getTitle(),
                videoSummary.getDescription(),
                Arrays.stream(videoSummary.getKeywords().split(",")).toList(),
                videoSummary.getUrl(),
                videoSummary.getSummary(),
                videoSummary.getAddress(),
                videoSummary.getCreatedAt(),
                videoSummary.getPlatform()
        );
    }

}
