package com.hongik.graduationproject.domain.entity;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryDto;
import com.hongik.graduationproject.domain.entity.global.BaseTimeEntity;
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
public class VideoSummaryRDB extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_summary_id")
    private Long id;
    private String title;
    @Column(length = 1000)
    private String description;
    private String keywords;
    private String url;
    @Column(length = 1000)
    private String summary;
    private String address;
    private String videoCode;

    public static VideoSummaryRDB of(VideoSummaryDto videoSummaryDto) {
        return VideoSummaryRDB
                .builder()
                .videoCode(videoSummaryDto.getVideoCode())
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
        if (keywords != null && !keywords.isEmpty()) {
            for (String keyword : keywords) {
                sb.append(keyword).append(',');
            }
            return sb.substring(0, sb.length() - 2);
        } else {
            return "";
        }
    }
}
