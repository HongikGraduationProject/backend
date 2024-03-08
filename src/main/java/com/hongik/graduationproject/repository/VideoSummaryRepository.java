package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.VideoSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VideoSummaryRepository extends MongoRepository<VideoSummary, String> {
    Optional<VideoSummary> findByUuid(String uuid);
    boolean existsByUuid(String uuid);
}
