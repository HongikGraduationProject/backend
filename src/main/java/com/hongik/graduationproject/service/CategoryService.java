package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.category.SubCategoryListResponse;
import com.hongik.graduationproject.domain.dto.category.SubCategoryResponse;
import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.domain.entity.VideoSummaryCategory;
import com.hongik.graduationproject.enums.MainCategory;
import com.hongik.graduationproject.repository.CategoryRepository;
import com.hongik.graduationproject.repository.UserRepository;
import com.hongik.graduationproject.repository.VideoSummaryCategoryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final VideoSummaryCategoryRepository videoSummaryCategoryRepository;

    public SubCategoryListResponse getSubCategoryList(MainCategory mainCategory) {
        User user = userRepository.getReferenceById(1L);
     // MainCategory mainCategory = subCategoryListRequest.mainCategory();

        List<Category> categories = categoryRepository.findAllByMainCategoryAndUser(mainCategory, user);

        for (Category category : categories) {
            updateCategorySummary(category);
        }

        List<SubCategoryResponse> subCategoryList = categories.stream()
                .map(category -> new SubCategoryResponse(category.getSubCategory(), category.getId(), category.getSummaryCount(), category.getUpdateAt()))
                .toList();

        return new SubCategoryListResponse(subCategoryList);
    }

    private void updateCategorySummary(Category category) {
        List<VideoSummaryCategory> videoSummaryCategories = videoSummaryCategoryRepository.findAllByCategory(category);
        int videoCount = videoSummaryCategories.size();
        LocalDateTime latestUpdateAt = videoSummaryCategories.stream()
                .map(VideoSummaryCategory::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        category.updateSummaryCount(videoCount);
        category.updateUpdateAt(latestUpdateAt);

        categoryRepository.save(category);
    }
}