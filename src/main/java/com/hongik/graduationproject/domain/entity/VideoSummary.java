package com.hongik.graduationproject.domain.entity;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Document(collection = "video_summary")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoSummary {
    private String uuid;
    private String title;
    private String description;
    private List<String> keywords;
    private String url;
    private String summary;

    public VideoSummary(VideoSummaryMessage videoSummaryMessage) {
        uuid = videoSummaryMessage.getUuid();
        title = videoSummaryMessage.getTitle();
        description = videoSummaryMessage.getDescription();
        keywords = videoSummaryMessage.getKeywords();
        url = videoSummaryMessage.getUrl();
        summary = videoSummaryMessage.getSummary();
    }
}
