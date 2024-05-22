package com.hongik.graduationproject.repository;


import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VideoSummaryStatusCacheRepository extends CrudRepository<VideoSummaryStatusCache, String> {
    Optional<VideoSummaryStatusCache> findFirstByVideoCode(String videoCode);

    boolean existsByVideoCode(String videoCode);

    boolean existsByVideoCodeAndUserId(String videoCode, Long userId);
}
