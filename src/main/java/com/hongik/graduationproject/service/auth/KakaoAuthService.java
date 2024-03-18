package com.hongik.graduationproject.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.graduationproject.domain.auth.oauth.OauthToken;
import com.hongik.graduationproject.domain.dto.ApiResponse;
import com.hongik.graduationproject.domain.dto.AuthRequestDto;
import com.hongik.graduationproject.domain.dto.KaKaoResponseDto;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.domain.dto.KaKaoRequestDto;
import com.hongik.graduationproject.domain.auth.oauth.KaKaoProfile;
import com.hongik.graduationproject.jwt.TokenProvider;
import com.hongik.graduationproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoAuthService implements AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public ApiResponse<?> loginUser(AuthRequestDto authRequestDto) {

        KaKaoRequestDto kakaoRequestDto = (KaKaoRequestDto) authRequestDto;
        String accessToken = kakaoRequestDto.getAccessToken();

        OauthToken oauthToken = getAccessToken(accessToken);
        KaKaoProfile kakaoProfile = getKaKaoProfile(oauthToken.getAccessToken());
        User user = userRepository.findByEmail(kakaoProfile.getKakao_account().getEmail());

        if (user == null) {
            user = User.builder()
                    .kakaoId(kakaoProfile.getId())
                    .kakaoNickname(kakaoProfile.getKakao_account().getProfile().getNickname())
                    .email(kakaoProfile.getKakao_account().getEmail())
                    .build();
            User savedUser = userRepository.save(user);

            String newAccessToken = tokenProvider.create(savedUser.getId());
            String refreshToken = tokenProvider.refresh(newAccessToken);
            int exprTime = 3600000;

            KaKaoResponseDto kaKaoResponseDto = new KaKaoResponseDto(newAccessToken, refreshToken, exprTime, user);
            return ApiResponse.createSuccess(kaKaoResponseDto);
        }

        return ApiResponse.createError("User already exists");
    }

    private OauthToken getAccessToken(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "{8f7f711ce205744c2b26973c36d44708}"); //내가 발급해서 넣음
        params.add("redirect_uri", "{http://127.0.0.1:8080/account/sign-in/kakao/callback}"); //임의 생성

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthToken;
    }

    private KaKaoProfile getKaKaoProfile(String token) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("token = " + token);
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KaKaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KaKaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return kakaoProfile;
    }
}
