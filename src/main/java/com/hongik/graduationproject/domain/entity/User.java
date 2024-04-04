package com.hongik.graduationproject.domain.entity;

import com.hongik.graduationproject.domain.dto.auth.oauth.KaKaoProfile;
import com.hongik.graduationproject.domain.entity.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String kakaoNickname;
    private Long kakaoId;
    private String email;

    public static User of(KaKaoProfile kakaoProfile) {
        return User.builder()
                .kakaoId(kakaoProfile.getId())
                .kakaoNickname(kakaoProfile.getKakaoAccount().getProfile().getNickname())
                .email(kakaoProfile.getKakaoAccount().getEmail())
                .build();
    }
}
