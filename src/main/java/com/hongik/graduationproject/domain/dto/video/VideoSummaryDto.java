package com.hongik.graduationproject.domain.dto.video;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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


//    public static VideoSummaryDto from(VideoSummary videoSummary) {
//        return VideoSummaryDto.builder()
//                .videoCode(videoSummary.())
//                .title(videoSummary.getTitle())
//                .description(videoSummary.getDescription())
//                .keywords(videoSummary.getKeywords())
//                .url(videoSummary.getUrl())
//                .summary(videoSummary.getSummary())
//                .address(videoSummary.getAddress())
//                .createdAt(videoSummary.getCreatedAt().minusHours(9))
//                .build();
//    }
//

}
