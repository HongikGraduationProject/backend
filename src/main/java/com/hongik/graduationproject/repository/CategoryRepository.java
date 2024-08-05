package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.MainCategoryCount;
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

    @Query(value = "SELECT c.main_category AS mainCategory, COUNT(vsc.video_summary_category_id) AS count " +
            "FROM category c LEFT JOIN video_summary_category vsc ON c.category_id = vsc.category_id " +
            "WHERE c.user_id = :userId " +
            "GROUP BY c.main_category " +
            "ORDER BY COUNT(vsc.video_summary_category_id) DESC " +
            "LIMIT 2", nativeQuery = true)
    List<MainCategoryCount> getSummaryCountOfMainCategoryByUser(Long userId);
}