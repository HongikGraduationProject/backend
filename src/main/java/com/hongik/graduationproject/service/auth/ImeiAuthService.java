package com.hongik.graduationproject.service.auth;

import com.hongik.graduationproject.domain.dto.auth.IssueRequest;
import com.hongik.graduationproject.domain.dto.auth.IssueTokenResponse;
import com.hongik.graduationproject.domain.dto.auth.ReissueRequest;
import com.hongik.graduationproject.domain.dto.auth.ReissueResponse;
import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.enums.MainCategory;
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

    public IssueTokenResponse issueTokenFromImei(IssueRequest issueRequest) {
        User user;
        if (!userRepository.existsByImei(issueRequest.imei())) {
            user = createUser(issueRequest);
        } else {
            user = userRepository.findByImei(issueRequest.imei()).get();
        }

        String accessToken = tokenProvider.createAccessToken(user.getId());

        return new IssueTokenResponse(accessToken);
    }

    private User createUser(IssueRequest issueRequest) {
        User user;
        user = userRepository.save(User.builder()
                .imei(issueRequest.imei())
                .build());

        createCategories(user);
        return user;
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
}
