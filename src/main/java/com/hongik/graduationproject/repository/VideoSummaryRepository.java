package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.VideoSummaryRDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoSummaryRepository extends JpaRepository<VideoSummaryRDB, Long> {
    Optional<VideoSummaryRDB> findById(Long id);
    boolean existsById(Long id);
}
