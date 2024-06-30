package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.VideoSummaryCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoSummaryCategoryRepository extends JpaRepository<VideoSummaryCategory, Long> {
    List<VideoSummaryCategory> findAllByCategory(Category category);
    VideoSummaryCategory findByVideoSummary(VideoSummary videoSummary);
    VideoSummaryCategory findFirstByVideoSummary(VideoSummary videoSummary);
    boolean existsByCategoryAndVideoSummary(Category category, VideoSummary videoSummary);

    @EntityGraph(attributePaths = {"category", "category.user", "videoSummary"})
    @Query("SELECT COUNT(vsc) > 0 " +
            "FROM VideoSummaryCategory vsc " +
            "WHERE vsc.category.user.id = :userId " +
            "AND vsc.videoSummary.videoCode = :videoCode")
    boolean existsByVideoCodeAndUserId(String videoCode, Long userId);
}
