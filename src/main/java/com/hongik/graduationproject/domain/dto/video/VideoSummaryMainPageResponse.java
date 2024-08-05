package com.hongik.graduationproject.domain.dto.video;

import java.time.LocalDateTime;

import com.hongik.graduationproject.domain.entity.VideoSummaryCategory;
import com.hongik.graduationproject.enums.MainCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VideoSummaryMainPageResponse {
	private String title;
	private MainCategory mainCategory;
	private LocalDateTime createdAt;

	public static VideoSummaryMainPageResponse of(VideoSummaryCategory videoSummaryCategory) {
		return new VideoSummaryMainPageResponse(
			videoSummaryCategory.getVideoSummary().getTitle(),
			videoSummaryCategory.getCategory().getMainCategory(),
			videoSummaryCategory.getCreatedAt()
		);
	}
}
