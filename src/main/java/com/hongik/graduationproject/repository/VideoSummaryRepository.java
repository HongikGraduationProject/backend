package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.VideoSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoSummaryRepository extends JpaRepository<VideoSummary, Long> {
    Optional<VideoSummary> findById(Long id);
    boolean existsById(Long id);
    boolean existsByVideoCode(String videoCode);
    Optional<VideoSummary> findByVideoCode(String videoCode);
}
