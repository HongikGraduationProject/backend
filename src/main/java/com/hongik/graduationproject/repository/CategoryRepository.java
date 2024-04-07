package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.eum.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.user.id = :userId and c.mainCategory = :mainCategory and c.subCategory = '기타'")
    Optional<Category> findDefaultCategoryByUserIdAndMainCategory(Long userId, MainCategory mainCategory);
}
