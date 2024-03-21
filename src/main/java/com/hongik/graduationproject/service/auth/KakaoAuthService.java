package com.hongik.graduationproject.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.graduationproject.domain.auth.oauth.OauthToken;
import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.AuthRequestDto;
import com.hongik.graduationproject.domain.dto.KaKaoResponseDto;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.domain.dto.KaKaoRequestDto;
import com.hongik.graduationproject.domain.auth.oauth.KaKaoProfile;
import com.hongik.graduationproject.jwt.TokenProvider;
import com.hongik.graduationproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthService implements AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);
    private final ObjectMapper objectMapper;

    @Override
    public Response<?> loginUser(AuthRequestDto authRequestDto) {

        KaKaoRequestDto kakaoRequestDto = (KaKaoRequestDto) authRequestDto;
        KaKaoProfile kakaoProfile = getKaKaoProfile(kakaoRequestDto.getAccessToken());

        if (kakaoProfile == null || kakaoProfile.getKakao_account() == null) {
            logger.error("Kakao profile or account information is null");
            return Response.createError("Failed to retrieve Kakao profile or account information");
        }

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
            return Response.createSuccess(kaKaoResponseDto);
        }

        return Response.createError("User already exists");
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

        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse access token response: {}", e.getMessage());
        }

        return oauthToken;
    }

    private KaKaoProfile getKaKaoProfile(String token) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        try {
            ResponseEntity<KaKaoProfile> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    kakaoProfileRequest,
                    KaKaoProfile.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Failed to get Kakao profile: {}", e.getMessage());
            return null;
        }
    }
}
