package com.hongik.graduationproject.domain.dto.video;

import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.enums.Platform;

import java.util.Arrays;
import java.util.List;

public record VideoSummaryResponse(
        String title,
        List<String> keywords,
        String url,
        String address,
        Platform platform
) {
    public VideoSummaryResponse(VideoSummary videoSummary) {
        this(
                videoSummary.getTitle(),
                Arrays.stream(videoSummary.getKeywords().split(",")).toList(),
                videoSummary.getUrl(),
                videoSummary.getAddress(),
                videoSummary.getPlatform()
        );
    }

}
