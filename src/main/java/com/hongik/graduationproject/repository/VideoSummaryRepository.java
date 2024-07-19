package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.VideoSummary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoSummaryRepository extends JpaRepository<VideoSummary, Long> {
    Optional<VideoSummary> findById(Long id);
    boolean existsById(Long id);
    Optional<VideoSummary> findByVideoCode(String videoCode);

    @Query("SELECT vs.id FROM VideoSummary vs " +
            "WHERE vs.title LIKE CONCAT('%', :searchword, '%')"
            + "OR vs.description LIKE CONCAT('%', :searchword, '%')"
            + "OR vs.summary LIKE CONCAT('%', :searchword, '%')"
            + "OR vs.keywords LIKE CONCAT('%', :searchword, '%')")
    List<Long> getAllVideoIdsBySearchWord(@Param("searchword") String searchWord);

    List<VideoSummary> findAllByIsDeletedTrue();
}