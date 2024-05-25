package com.hongik.graduationproject.domain.entity;

import com.hongik.graduationproject.domain.entity.global.BaseTimeEntity;
import com.hongik.graduationproject.enums.MainCategory;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private MainCategory mainCategory;
    private String subCategory;

    private Integer summaryCount;
    private LocalDateTime updateAt;

    public void updateSummaryCount(int count) {
        this.summaryCount = count;
    }

    public void updateUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}