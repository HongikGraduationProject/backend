package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.VideoSummaryCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoSummaryCategoryRepository extends JpaRepository<VideoSummaryCategory, Long> {
    List<VideoSummaryCategory> findAllByCategory(Category category);
}
