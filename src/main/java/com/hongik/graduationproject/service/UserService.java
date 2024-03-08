package com.hongik.graduationproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.domain.oauth.KaKaoProfile;
import com.hongik.graduationproject.domain.oauth.KaKaoRequestDto;
import com.hongik.graduationproject.domain.oauth.KaKaoResponseDto;
import com.hongik.graduationproject.domain.oauth.OauthToken;
import com.hongik.graduationproject.jwt.TokenProvider;
import com.hongik.graduationproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public OauthToken getAccessToken(KaKaoRequestDto kaKaoRequestDto) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "{b934dbc48ec4f507b10d7720ec3fbcaa}"); //새로 발급 받은 키 넣었음
        params.add("redirect_uri", "{}");

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return oauthToken;
    }

    public KaKaoResponseDto saveUser(KaKaoRequestDto kaKaoRequestDto) {
        KaKaoProfile kakaoProfile = findUser(kaKaoRequestDto.getAccessToken());

        Optional<User> user = Optional.ofNullable(
                userRepository.findByEmail(kakaoProfile.getKakao_account().getEmail()));

        if (!user.isPresent()) {
            User newUser = User.builder()
                    .kakaoId(kakaoProfile.getId())
                    .kakaoNickname(kakaoProfile.kakao_account.getProfile().getNickname())
                    .email(kakaoProfile.getKakao_account().getEmail())
                    .build();

            User savedUser = userRepository.save(newUser);

            String accessToken = tokenProvider.create(savedUser.getId());
            String refreshToken = tokenProvider.refresh(accessToken);
            int exprTime = 3600000;

            KaKaoResponseDto kaKaoResponseDto = new KaKaoResponseDto(accessToken, refreshToken, exprTime, savedUser);
            return kaKaoResponseDto;
        }

        return  null;
    }

    public KaKaoProfile findUser(String token) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("token = " + token);
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
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

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }
}