package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.VideoSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoSummaryRepository extends JpaRepository<VideoSummary, Long> {
    Optional<VideoSummary> findById(Long id);

    boolean existsById(Long id);

    Optional<VideoSummary> findByVideoCode(String videoCode);

    @Query("SELECT vsc.videoSummary.id FROM VideoSummaryCategory vsc " +
            "WHERE vsc.category.user.id = :userId " +
            "AND (vsc.videoSummary.title LIKE CONCAT('%', :searchWord, '%') " +
            "OR vsc.videoSummary.description LIKE CONCAT('%', :searchWord, '%') " +
            "OR vsc.videoSummary.summary LIKE CONCAT('%', :searchWord, '%') " +
            "OR vsc.videoSummary.keywords LIKE CONCAT('%', :searchWord, '%')) " +
            "AND vsc.videoSummary.isDeleted = false"
    )
    List<Long> getAllUserVideoIdsBySearchWord(@Param("userId") Long userId,
                                              @Param("searchWord") String searchWord);

    List<VideoSummary> findAllByIsDeletedTrue();

    @Query("SELECT vs FROM VideoSummary vs WHERE vs.id = :videoSummaryId AND vs.isDeleted = true")
    Optional<VideoSummary> findDeletedById(@Param("videoSummaryId") Long videoSummaryId);
}