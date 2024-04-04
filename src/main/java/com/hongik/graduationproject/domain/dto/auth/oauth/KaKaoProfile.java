package com.hongik.graduationproject.domain.dto.auth.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaKaoProfile {

    public Long id;
    public String connectedAt;
    public KakaoProperties properties;
    public KakaoAccount kakaoAccount;

    @Data
    public static class KakaoProperties {

        public String profileImage;
        public String thumbnailImage;
    }

    @Data
    public static class KakaoAccount {

        public Boolean profileNicknameNeedsAgreement;
        public Boolean profileImageNeedsAgreement;
        public Profile profile;
        public Boolean hasEmail;
        public Boolean emailNeedsAgreement;
        public Boolean isEmailValid;
        public Boolean isEmailVerified;
        public String email;

        @Data
        public static class Profile {

            public String nickname;
            public String thumbnailImageUrl;
            public String profileImageUrl;
            public Boolean isDefaultImage;
        }
    }
}
