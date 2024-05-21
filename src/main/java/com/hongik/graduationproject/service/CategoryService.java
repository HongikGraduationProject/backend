package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.category.SubCategoryListRequest;
import com.hongik.graduationproject.domain.dto.category.SubCategoryListResponse;
import com.hongik.graduationproject.domain.dto.category.SubCategoryResponse;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.eum.MainCategory;
import com.hongik.graduationproject.repository.CategoryRepository;
import com.hongik.graduationproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public SubCategoryListResponse getSubCategoryList(MainCategory mainCategory, Long userId) {
//        User user = userRepository.getReferenceById(userId);
        User user = userRepository.getReferenceById(1L);

        List<SubCategoryResponse> subCategoryList = categoryRepository.findAllByMainCategoryAndUser(mainCategory, user).stream()
                .map(category -> new SubCategoryResponse(category.getSubCategory(), category.getId()))
                .toList();
        return new SubCategoryListResponse(subCategoryList);
    }
}
