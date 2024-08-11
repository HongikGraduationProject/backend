package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.MainCategoryCount;
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
import java.util.Date;
import java.util.stream.Collectors;
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
        List<MainCategoryCount> categoryCounts = categoryRepository.getSummaryCountOfMainCategoryByUser(userId);

        int totalSummaries = categoryCounts.stream()
                .mapToInt(count -> count.getCount().intValue())
                .sum();

        List<MainCategoryRankingResponse> rankingList = categoryCounts.stream()
                .map(count -> createRankingResponse(count, totalSummaries))
                .filter(ranking -> ranking.summaryCount() > 0)  // summaryCount가 0인 경우를 필터링
                .collect(Collectors.toList());

        log.info("사용자 ID {}의 메인 카테고리 순위 조회", userId);
        return new MainCategoryRankingListResponse(rankingList);
    }

    @Transactional
    public MainCategoryRankingListResponse getMainCategoryRankingByDate(Long userId, Date startDate, Date endDate) {
        List<MainCategoryCount> categoryCounts = categoryRepository.getSummaryCountOfMainCategoryByUserAndDate(userId, startDate, endDate);

        int totalSummaries = categoryCounts.stream()
                .mapToInt(count -> count.getCount().intValue())
                .sum();

        List<MainCategoryRankingResponse> rankingList = categoryCounts.stream()
                .map(count -> createRankingResponse(count, totalSummaries))
                .filter(ranking -> ranking.summaryCount() > 0)
                .collect(Collectors.toList());

        log.info("사용자 ID {}의 {}부터 {}까지의 메인 카테고리 순위 조회", userId, startDate, endDate);
        return new MainCategoryRankingListResponse(rankingList);
    }

    private MainCategoryRankingResponse createRankingResponse(MainCategoryCount count, int totalSummaries) {
        MainCategory mainCategory = MainCategory.valueOf(count.getMainCategory());
        int summaryCount = count.getCount().intValue();
        double percentage = totalSummaries > 0 ? (summaryCount / (double) totalSummaries) * 100 : 0.0;
        return new MainCategoryRankingResponse(mainCategory, percentage, summaryCount);
    }
}