package com.hongik.graduationproject.repository;


import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VideoSummaryStatusCacheRepository extends CrudRepository<VideoSummaryStatusCache, String> {
    Optional<VideoSummaryStatusCache> findFirstByVideoCode(String videoCode);
    List<VideoSummaryStatusCache> findAllByVideoCode(String videoCode);
    Optional<VideoSummaryStatusCache> findByVideoCodeAndUserId(String videoCode, Long userId);
    boolean existsByVideoCodeAndUserId(String videoCode, Long userId);
}
