package com.hongik.graduationproject.service.auth;

import com.hongik.graduationproject.domain.dto.auth.AuthRequest;
import com.hongik.graduationproject.domain.dto.auth.AuthResponse;
import com.hongik.graduationproject.domain.dto.auth.KaKaoRequest;
import com.hongik.graduationproject.domain.dto.auth.KaKaoResponse;
import com.hongik.graduationproject.domain.dto.auth.ReissueRequest;
import com.hongik.graduationproject.domain.dto.auth.ReissueResponse;
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
    public AuthResponse loginUser(AuthRequest authRequest) {

        KaKaoRequest kakaoRequest = (KaKaoRequest) authRequest;
        KaKaoProfile kakaoProfile = getKaKaoProfile(kakaoRequest.getAccessToken());

        if (kakaoProfile == null || kakaoProfile.getKakao_account() == null) {
            log.error("Failed to retrieve Kakao profile or account information");
            throw new RuntimeException(); //TODO: 예외 처리 요망
        }

        String email = kakaoProfile.getKakao_account().getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            throw new RuntimeException(); //TODO: 예외 처리 요망
        }

        User savedUser = userRepository.save(User.of(kakaoProfile));

        String newAccessToken = tokenProvider.createAccessToken(savedUser.getId());
        String refreshToken = tokenProvider.createRefreshToken(newAccessToken);
        int exprTime = 3600000;

        return new KaKaoResponse(newAccessToken, refreshToken, exprTime, savedUser);
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
    public ReissueResponse reissueToken(ReissueRequest reissueRequest) {

        Long userId = tokenProvider.getUserId(reissueRequest.getAccessToken());

        if (userId == null) {
            log.error("Failed to retrieve user information");
            throw new RuntimeException(); //TODO: 예외 처리 요망
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            log.error("User not found");
            throw new RuntimeException(); //TODO: 예외 처리 요망
        }

        tokenProvider.validate(reissueRequest.getAccessToken());

        String newAccessToken = tokenProvider.createAccessToken(userId);
        String newRefreshToken = tokenProvider.createRefreshToken(reissueRequest.getRefreshToken());
        int exprTime = 3600000;

        return new ReissueResponse(newAccessToken, newRefreshToken, exprTime);
    }
}