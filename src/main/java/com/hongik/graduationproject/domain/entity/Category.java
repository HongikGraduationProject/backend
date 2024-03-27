package com.hongik.graduationproject.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String mainCategory;
    private String subCategory;
}
