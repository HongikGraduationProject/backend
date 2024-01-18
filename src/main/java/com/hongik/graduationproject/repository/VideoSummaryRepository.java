package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.VideoSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoSummaryRepository extends MongoRepository<VideoSummary, String> {
    VideoSummary findByUuid(String uuid);
}
