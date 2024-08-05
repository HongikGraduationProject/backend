package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.VideoSummaryCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoSummaryCategoryRepository extends JpaRepository<VideoSummaryCategory, Long> {

    @Query("SELECT vsc " +
            "FROM VideoSummaryCategory vsc " +
            "WHERE vsc.category = :category " +
            "AND vsc.category.user = :user " +
            "AND vsc.videoSummary.isDeleted = false")
    List<VideoSummaryCategory> findAllByCategory(Category category, User user);

    @Query("select vsc " +
            "from VideoSummaryCategory vsc " +
            "where vsc.category.user = :user " +
            "and vsc.videoSummary = :videoSummary")
    VideoSummaryCategory findByVideoSummaryAndUser(VideoSummary videoSummary, User user);

    @EntityGraph(attributePaths = {"category", "category.user", "videoSummary"})
    @Query("SELECT COUNT(vsc) > 0 " +
            "FROM VideoSummaryCategory vsc " +
            "WHERE vsc.category.user.id = :userId " +
            "AND vsc.videoSummary.videoCode = :videoCode")
    boolean existsByVideoCodeAndUserId(String videoCode, Long userId);

    @EntityGraph(attributePaths = {"category", "videoSummary"})
    @Query("SELECT vsc "
        + "FROM VideoSummaryCategory vsc "
        + "WHERE vsc.category.user = :user "
        + "ORDER BY vsc.createdAt DESC")
    List<VideoSummaryCategory> findAllByUser(User user);
}
