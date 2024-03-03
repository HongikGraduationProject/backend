package com.hongik.graduationproject.domain.entity;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
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
    private String address;

    @Field("created_at")
    private LocalDateTime createdAt;

    public VideoSummary(VideoSummaryDto videoSummaryDto) {
        uuid = videoSummaryDto.getUuid();
        title = videoSummaryDto.getTitle();
        description = videoSummaryDto.getDescription();
        keywords = videoSummaryDto.getKeywords();
        url = videoSummaryDto.getUrl();
        summary = videoSummaryDto.getSummary();
        address = videoSummaryDto.getAddress();
        createdAt = LocalDateTime.now().plusHours(9);
    }
}
