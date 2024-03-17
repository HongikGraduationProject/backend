package com.hongik.graduationproject.repository;


import com.hongik.graduationproject.domain.entity.VideoSummaryStatusCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VideoSummaryStatusCacheRepository extends CrudRepository<VideoSummaryStatusCache, String> {
    Optional<VideoSummaryStatusCache> findByVideoCode(String videoCode);
}
