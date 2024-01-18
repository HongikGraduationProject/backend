package com.hongik.graduationproject.domain.dto.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoSummarizeRequest {
    String uuid;
    String title;
    String description;
    List<String> keywords;
    String url;
    String summary;
}
