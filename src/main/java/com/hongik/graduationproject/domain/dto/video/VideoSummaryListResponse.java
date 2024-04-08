package com.hongik.graduationproject.domain.dto.video;


import com.hongik.graduationproject.domain.entity.VideoSummary;

import java.util.List;

public record VideoSummaryListResponse(
        List<VideoSummaryResponse> videoSummaryList
) {
}
