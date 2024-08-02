package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.category.MainCategoryRankingListResponse;
import com.hongik.graduationproject.domain.dto.category.MainCategoryRankingResponse;
import com.hongik.graduationproject.domain.dto.category.SubCategoryListResponse;
import com.hongik.graduationproject.domain.dto.category.SubCategoryResponse;
import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.enums.MainCategory;
import com.hongik.graduationproject.exception.AppException;
import com.hongik.graduationproject.exception.ErrorCode;
import com.hongik.graduationproject.repository.CategoryRepository;
import com.hongik.graduationproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public SubCategoryListResponse getSubCategoryList(MainCategory mainCategory, Long userId) {
//        User user = userRepository.getReferenceById(1L);
        User user = userRepository.getReferenceById(userId);
        List<SubCategoryResponse> subCategoryList = categoryRepository.findAllByMainCategoryAndUser(mainCategory, user);
        log.info("사용자 ID {}의 메인 카테고리 {}에 대한 서브 카테고리 목록 조회", userId, mainCategory);
        return new SubCategoryListResponse(subCategoryList);
    }

    public SubCategoryListResponse getAllSubCategoryList(Long userId) {
        User user = userRepository.getReferenceById(userId);
        List<SubCategoryResponse> subCategoryList = categoryRepository.findAllSubCategoryByUser(user);
        return new SubCategoryListResponse(subCategoryList);
    }

    @Transactional
    public void createSubCategory(MainCategory mainCategory, String subCategoryName, Long userId) {

        User user = userRepository.getReferenceById(userId);

        if (categoryRepository.existsByMainCategoryAndSubCategory(mainCategory, subCategoryName)) {
            throw new AppException(ErrorCode.SUBCATEGORY_ALREADY_EXISTS);
        }

        Category category = Category.builder()
                .user(user)
                .mainCategory(mainCategory)
                .subCategory(subCategoryName)
                .build();

        categoryRepository.save(category);
    }

    @Transactional
    public MainCategoryRankingListResponse getMainCategoryRanking(Long userId) {
        User user = userRepository.getReferenceById(userId);
        List<MainCategoryRankingResponse> rankingList = categoryRepository.findMainCategoryRankingByUser(user);

        log.info("사용자 ID {}의 메인 카테고리 순위 조회", userId);
        return new MainCategoryRankingListResponse(rankingList);
    }
}