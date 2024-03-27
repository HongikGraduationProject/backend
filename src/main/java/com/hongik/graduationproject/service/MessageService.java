package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryDto;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryMessage;
import com.hongik.graduationproject.domain.entity.Category;
import com.hongik.graduationproject.domain.entity.User;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import com.hongik.graduationproject.repository.CategoryRepository;
import com.hongik.graduationproject.repository.UserRepository;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
import com.hongik.graduationproject.repository.VideoSummaryStatusCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.url.routing.key}")
    private String urlRoutingKey;
    private final RabbitTemplate rabbitTemplate;
    private final VideoSummaryRepository videoSummaryRepository;
    private final VideoSummaryStatusCacheRepository videoSummaryStatusCacheRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    public void sendVideoUrlToQueue(VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        log.info("Sent url: {}, videoCode: {}", videoSummaryInitiateRequest.getUrl(), videoSummaryInitiateRequest.getVideoCode());
        rabbitTemplate.convertAndSend(exchangeName, urlRoutingKey, videoSummaryInitiateRequest);
    }

    @RabbitListener(queues = "${rabbitmq.summary.queue.name}")
    @Transactional
    public void receiveVideoUrlFromQueue(VideoSummaryMessage videoSummaryMessage) {
        log.info("Received message: {}", videoSummaryMessage.toString());

        // TODO: 해당 사용자의 카테고리들을 찾고
        Optional<User> user = userRepository.findById(1L);
        Optional<Category> category = categoryRepository.findByUserId(user.get().getId());
        // TODO: 외래키 설정해서 함께 저장하는 기능 필요.
        VideoSummary savedVideoSummary = videoSummaryRepository.save(VideoSummary.of(videoSummaryMessage));

        updateStatusCache(videoSummaryMessage, savedVideoSummary);
    }

    private void updateStatusCache(VideoSummaryMessage videoSummaryMessage, VideoSummary savedVideoSummary) {
        VideoSummaryStatusCache statusCache = videoSummaryStatusCacheRepository.findById(videoSummaryMessage.getVideoCode()).get();

        statusCache.updateStatus("COMPLETE");
        statusCache.updateVideoSummaryId(savedVideoSummary.getId());

        videoSummaryStatusCacheRepository.save(statusCache);
    }

}
