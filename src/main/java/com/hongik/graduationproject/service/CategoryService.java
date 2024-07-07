package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.category.SubCategoryListResponse;
import com.hongik.graduationproject.domain.dto.category.SubCategoryResponse;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.enums.MainCategory;
import com.hongik.graduationproject.repository.CategoryRepository;
import com.hongik.graduationproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public SubCategoryListResponse getSubCategoryList(MainCategory mainCategory, Long userId) {
//        User user = userRepository.getReferenceById(1L);
        User user = userRepository.getReferenceById(userId);
        List<SubCategoryResponse> subCategoryList = categoryRepository.findAllByMainCategoryAndUser(mainCategory, user);
        return new SubCategoryListResponse(subCategoryList);
    }

    public SubCategoryListResponse getAllSubCategoryList(Long userId) {
        User user = userRepository.getReferenceById(userId);
        List<SubCategoryResponse> subCategoryList = categoryRepository.findAllSubCategoryByUser(user);
        return new SubCategoryListResponse(subCategoryList);
    }
}