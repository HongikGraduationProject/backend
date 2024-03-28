package com.hongik.graduationproject.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "video_summary_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoSummaryCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_summary_category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "video_summary_id")
    VideoSummary videoSummary;
}
