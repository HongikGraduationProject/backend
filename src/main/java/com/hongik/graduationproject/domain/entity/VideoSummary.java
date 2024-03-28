package com.hongik.graduationproject.domain.entity;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryDto;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryMessage;
import com.hongik.graduationproject.domain.entity.global.BaseTimeEntity;
import com.hongik.graduationproject.eum.Platform;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Table(name = "video_summary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VideoSummary extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_summary_id")
    private Long id;
    private String title;
    @Column(length = 2000)
    private String description;
    private String keywords;
    private String url;
    @Column(length = 2000)
    private String summary;
    private String address;
    private String videoCode;
    @Enumerated(EnumType.STRING)
    private Platform platform;


    public static VideoSummary of(VideoSummaryDto videoSummaryDto) {
        return VideoSummary
                .builder()
                .videoCode(videoSummaryDto.getVideoCode())
                .title(videoSummaryDto.getTitle())
                .description(videoSummaryDto.getDescription())
                .keywords(listToString(videoSummaryDto.getKeywords()))
                .url(videoSummaryDto.getUrl())
                .summary(videoSummaryDto.getSummary())
                .address(videoSummaryDto.getAddress())
                .platform(videoSummaryDto.getPlatform())
                .build();
    }

    public static VideoSummary of(VideoSummaryMessage videoSummaryMessage) {
        return VideoSummary
                .builder()
                .videoCode(videoSummaryMessage.getVideoCode())
                .title(videoSummaryMessage.getTitle())
                .description(videoSummaryMessage.getDescription())
                .keywords(listToString(videoSummaryMessage.getKeywords()))
                .url(videoSummaryMessage.getUrl())
                .summary(videoSummaryMessage.getSummary())
                .address(videoSummaryMessage.getAddress())
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
