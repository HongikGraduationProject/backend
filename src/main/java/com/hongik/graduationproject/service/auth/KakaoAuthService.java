package com.hongik.graduationproject.service.auth;

import com.hongik.graduationproject.domain.dto.AuthRequestDto;
import com.hongik.graduationproject.domain.dto.KaKaoRequestDto;
import com.hongik.graduationproject.domain.dto.KaKaoResponseDto;
import com.hongik.graduationproject.domain.dto.Response;
import com.hongik.graduationproject.domain.dto.auth.oauth.KaKaoProfile;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.jwt.TokenProvider;
import com.hongik.graduationproject.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthService implements AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public Response<?> loginUser(AuthRequestDto authRequestDto) {

        KaKaoRequestDto kakaoRequestDto = (KaKaoRequestDto) authRequestDto;
        KaKaoProfile kakaoProfile = getKaKaoProfile(kakaoRequestDto.getAccessToken());

        if (kakaoProfile == null || kakaoProfile.getKakaoAccount() == null) {
            log.error("Failed to retrieve Kakao profile or account information");
            return Response.createError("Failed to retrieve Kakao profile or account information");
        }

        Optional<User> optionalUser = userRepository.findByEmail(kakaoProfile.getKakaoAccount().getEmail());

        if (optionalUser.isPresent()) {
            return Response.createError("User already exists");
        }

        User user = User.builder()
                .kakaoId(kakaoProfile.getId())
                .kakaoNickname(kakaoProfile.getKakaoAccount().getProfile().getNickname())
                .email(kakaoProfile.getKakaoAccount().getEmail())
                .build();
        User savedUser = userRepository.save(user);

        String newAccessToken = tokenProvider.create(savedUser.getId());
        String refreshToken = tokenProvider.refresh(newAccessToken);
        int exprTime = 3600000;

        KaKaoResponseDto kaKaoResponseDto = new KaKaoResponseDto(newAccessToken, refreshToken, exprTime, user);
        return Response.createSuccess(kaKaoResponseDto);
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
            log.error("Failed to get Kakao profile: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Response<?> reissueToken(KaKaoRequestDto kaKaoRequestDto) {
        Long userId = tokenProvider.getUserId(kaKaoRequestDto.getAccessToken());

        if (userId == null) {
            log.error("Failed to retrieve user information");
            return Response.createError("Failed to retrieve user information");
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            log.error("User not found");
            return Response.createError("User not found");
        }

        User user = optionalUser.get();

        String newAccessToken = tokenProvider.create(userId);
        String newRefreshToken = tokenProvider.refresh(kaKaoRequestDto.getRefreshToken());
        int exprTime = 3600000;

        KaKaoResponseDto kaKaoResponseDto = new KaKaoResponseDto(newAccessToken, newRefreshToken, exprTime, user);
        return Response.createSuccess(kaKaoResponseDto);
    }
}