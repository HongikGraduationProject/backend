package com.hongik.graduationproject.domain.entity;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryMessage;
import com.hongik.graduationproject.domain.entity.global.BaseTimeEntity;
import com.hongik.graduationproject.enums.MainCategory;
import com.hongik.graduationproject.enums.Platform;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
    @Enumerated(EnumType.STRING)
    private MainCategory generatedMainCategory;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    private Double latitude;
    private Double longitude;

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
                .platform(videoSummaryMessage.getPlatform())
                .generatedMainCategory(MainCategory.find(videoSummaryMessage.getGeneratedMainCategoryName()))
                .build();
    }

    private static String listToString(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        if (keywords != null && !keywords.isEmpty()) {
            for (String keyword : keywords) {
                sb.append(keyword).append(',');
            }
            return sb.substring(0, sb.length() - 1);
        } else {
            return "";
        }
    }

    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
    }

    public void updateLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void updateLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
