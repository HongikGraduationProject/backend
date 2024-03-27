package com.hongik.graduationproject.domain.dto.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoSummaryMessage {
    @JsonProperty("video_code")
    String videoCode;
    String title;
    String description;
    List<String> keywords;
    String url;
    String summary;
    String address;
    @JsonProperty("video_code")
    String categoryName;
}
