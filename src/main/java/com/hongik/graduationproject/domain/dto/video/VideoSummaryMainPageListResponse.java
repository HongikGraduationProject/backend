package com.hongik.graduationproject.domain.dto.video;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VideoSummaryMainPageListResponse {
	private List<VideoSummaryMainPageResponse> videoSummaryList;
}
