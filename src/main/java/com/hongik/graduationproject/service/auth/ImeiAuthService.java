package com.hongik.graduationproject.service.auth;

import com.hongik.graduationproject.domain.dto.auth.ImeiJoinRequest;
import com.hongik.graduationproject.domain.dto.auth.ImeiJoinResponse;
import com.hongik.graduationproject.domain.dto.auth.ReissueRequest;
import com.hongik.graduationproject.domain.dto.auth.ReissueResponse;
import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.eum.MainCategory;
import com.hongik.graduationproject.exception.AppException;
import com.hongik.graduationproject.exception.ErrorCode;
import com.hongik.graduationproject.jwt.TokenProvider;
import com.hongik.graduationproject.repository.CategoryRepository;
import com.hongik.graduationproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImeiAuthService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TokenProvider tokenProvider;

    public ImeiJoinResponse joinUserWithImei(ImeiJoinRequest imeiJoinRequest) {
        checkDuplicateUser(imeiJoinRequest);

        User savedUser = userRepository.save(User.builder()
                            .imei(imeiJoinRequest.imei())
                            .build());

        createCategories(savedUser);

        String accessToken = tokenProvider.createAccessToken(savedUser.getId());
        String refreshToken = tokenProvider.createRefreshToken(savedUser.getId());

        return new ImeiJoinResponse(accessToken, refreshToken);
    }

    public ReissueResponse reissueToken(ReissueRequest reissueRequest) {
        Long userId = tokenProvider.parseUserId(reissueRequest.getAccessToken());

        tokenProvider.validateToken(reissueRequest.getRefreshToken());

        String newAccessToken = tokenProvider.createAccessToken(userId);
        String newRefreshToken = tokenProvider.createRefreshToken(userId);

        return new ReissueResponse(newAccessToken, newRefreshToken);
    }

    private void createCategories(User savedUser) {
        List<Category> categoryList = new ArrayList<>();
        for (MainCategory mainCategory : MainCategory.values()) {
            categoryList.add(Category.builder()
                            .user(savedUser)
                            .mainCategory(mainCategory)
                            .subCategory("기타")
                            .build());
        }
        categoryRepository.saveAll(categoryList);
    }

    private void checkDuplicateUser(ImeiJoinRequest imeiJoinRequest) {
        if (userRepository.existsByImei(imeiJoinRequest.imei())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }


}
