package com.hongik.graduationproject.domain.dto.auth.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaKaoProfile {

    public Long id;
    public String connected_at;
    public KakaoProperties properties;
    public KakaoAccount kakao_account;

    @Data
    public static class KakaoProperties {

        public String profile_image;
        public String thumbnail_image;
    }

    @Data
    public static class KakaoAccount {

        public Boolean profile_nickname_needs_agreement;
        public Boolean profile_image_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        @Data
        public static class Profile {

            public String nickname;
            public String thumbnail_image_url;
            public String profile_image_url;
            public Boolean is_default_image;
        }
    }
}