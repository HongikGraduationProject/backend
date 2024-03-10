package com.hongik.graduationproject.domain.entity;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "video_summary")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoSummaryRDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_summary_id")
    private Long videoSummaryId;
    private String title;
    private String description;
    private String keywords;
    private String url;
    private String summary;
    private String address;
    @CreatedDate
    private LocalDateTime createdAt;

    public static VideoSummaryRDB of(VideoSummaryDto videoSummaryDto) {
        return VideoSummaryRDB
                .builder()
                .title(videoSummaryDto.getTitle())
                .description(videoSummaryDto.getDescription())
                .keywords(listToString(videoSummaryDto.getKeywords()))
                .url(videoSummaryDto.getUrl())
                .summary(videoSummaryDto.getSummary())
                .address(videoSummaryDto.getAddress())
                .build();
    }

    private static String listToString(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        for (String keyword : keywords) {
            sb.append(keyword).append(',');
        }

        return sb.substring(0, sb.length() - 2);
    }
}
