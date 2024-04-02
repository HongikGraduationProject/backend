package com.hongik.graduationproject.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String kakaoNickname;
    private Long kakaoId;
    private String email;
    private String nickname;

    @CreatedDate
    private LocalDateTime createAt;
}
