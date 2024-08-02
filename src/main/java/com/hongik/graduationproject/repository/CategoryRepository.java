package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.dto.category.MainCategoryRankingResponse;
import com.hongik.graduationproject.domain.dto.category.SubCategoryResponse;
import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.enums.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.user.id = :userId and c.mainCategory = :mainCategory and c.subCategory = '기타'")
    Optional<Category> findDefaultCategoryByUserIdAndMainCategory(Long userId, MainCategory mainCategory);

    @Query("SELECT new com.hongik.graduationproject.domain.dto.category.SubCategoryResponse(" +
            "c.subCategory, c.id, CAST(COUNT(v.id) AS integer), MAX(v.createdAt)) " +
            "FROM Category c LEFT JOIN VideoSummaryCategory v ON c.id = v.category.id " +
            "WHERE c.mainCategory = :mainCategory AND c.user = :user " +
            "GROUP BY c.id, c.subCategory")
    List<SubCategoryResponse> findAllByMainCategoryAndUser(MainCategory mainCategory, User user);

    @Query("SELECT new com.hongik.graduationproject.domain.dto.category.SubCategoryResponse(" +
            "c.subCategory, c.id, CAST(COUNT(v.id) AS integer), MAX(v.createdAt)) " +
            "FROM Category c LEFT JOIN VideoSummaryCategory v ON c.id = v.category.id " +
            "WHERE c.user = :user " +
            "GROUP BY c.subCategory, c.id")
    List<SubCategoryResponse> findAllSubCategoryByUser(User user);

    boolean existsByMainCategoryAndSubCategory(MainCategory mainCategory, String subCategory);

    @Query("SELECT new com.hongik.graduationproject.domain.dto.category.MainCategoryRankingResponse(" +
            "c.mainCategory, " +
            "ROUND((CAST(COUNT(v.id) AS double) / (SELECT COUNT(v1.id) FROM VideoSummaryCategory v1)) * 100, 1), " +
            "CAST(COUNT(v.id) AS integer)) " +
            "FROM Category c LEFT JOIN VideoSummaryCategory v ON c.id = v.category.id " +
            "WHERE c.user = :user " +
            "GROUP BY c.mainCategory " +
            "ORDER BY COUNT(v.id) DESC LIMIT 2")
    List<MainCategoryRankingResponse> findMainCategoryRankingByUser(User user);
}