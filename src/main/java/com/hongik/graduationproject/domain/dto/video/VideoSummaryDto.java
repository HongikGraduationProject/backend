package com.hongik.graduationproject.domain.dto.video;

import com.hongik.graduationproject.domain.entity.VideoSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoSummaryDto {
    String uuid;
    String title;
    String description;
    List<String> keywords;
    String url;
    String summary;
    public static VideoSummaryDto toDto(VideoSummary videoSummary){
        return VideoSummaryDto.builder()
                .uuid(videoSummary.getUuid())
                .title(videoSummary.getTitle())
                .description(videoSummary.getDescription())
                .keywords(videoSummary.getKeywords())
                .url(videoSummary.getUrl())
                .summary(videoSummary.getSummary())
                .build();
    }
}
