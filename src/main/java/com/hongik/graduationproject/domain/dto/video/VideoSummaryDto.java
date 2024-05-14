package com.hongik.graduationproject.domain.dto.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.VideoSummaryCategory;
import com.hongik.graduationproject.eum.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoSummaryDto {
    @JsonProperty("video_code")
    String videoCode;
    String title;
    String description;
    List<String> keywords;
    String url;
    String summary;
    String address;
    LocalDateTime createdAt;
    Platform platform;
    String mainCategory;
    int mainCategoryIndex;
    String subCategory;
    Long subCategoryId;

    public static VideoSummaryDto from(VideoSummary videoSummary) {
        return VideoSummaryDto.builder()
                .videoCode(videoSummary.getVideoCode())
                .title(videoSummary.getTitle())
                .description(videoSummary.getDescription())
                .keywords(Arrays.stream(videoSummary.getKeywords().split(",")).toList())
                .url(videoSummary.getUrl())
                .summary(videoSummary.getSummary())
                .address(videoSummary.getAddress())
                .createdAt(videoSummary.getCreatedAt().minusHours(9))
                .platform(videoSummary.getPlatform())
                .mainCategory(videoSummary.getGeneratedMainCategory().getName())
                .build();
    }

    public static VideoSummaryDto from(VideoSummaryCategory videoSummaryCategory) {
        return VideoSummaryDto.builder()
                .videoCode(videoSummaryCategory.getVideoSummary().getVideoCode())
                .title(videoSummaryCategory.getVideoSummary().getTitle())
                .description(videoSummaryCategory.getVideoSummary().getDescription())
                .keywords(Arrays.stream(videoSummaryCategory.getVideoSummary().getKeywords().split(",")).toList())
                .url(videoSummaryCategory.getVideoSummary().getUrl())
                .summary(videoSummaryCategory.getVideoSummary().getSummary())
                .address(videoSummaryCategory.getVideoSummary().getAddress())
                .createdAt(videoSummaryCategory.getVideoSummary().getCreatedAt().minusHours(9))
                .platform(videoSummaryCategory.getVideoSummary().getPlatform())
                .mainCategory(videoSummaryCategory.getVideoSummary().getGeneratedMainCategory().getName())
                .mainCategoryIndex(videoSummaryCategory.getVideoSummary().getGeneratedMainCategory().getIndex())
                .subCategory(videoSummaryCategory.getCategory().getSubCategory())
                .subCategoryId(videoSummaryCategory.getCategory().getId())
                .build();
    }
}
