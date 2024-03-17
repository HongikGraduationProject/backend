package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryDto;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.VideoSummaryRDB;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
import com.hongik.graduationproject.repository.VideoSummaryStatusCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

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

    public void sendVideoUrlToQueue(VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        log.info("Sent url: {}, videoCode: {}", videoSummaryInitiateRequest.getUrl(), videoSummaryInitiateRequest.getVideoCode());
        rabbitTemplate.convertAndSend(exchangeName, urlRoutingKey, videoSummaryInitiateRequest);
    }

    @RabbitListener(queues = "${rabbitmq.summary.queue.name}")
    public void receiveVideoUrlFromQueue(VideoSummaryDto videoSummaryDto) {
        log.info("Received message: {}", videoSummaryDto.toString());
        videoSummaryRepository.save(VideoSummaryRDB.of(videoSummaryDto));
    }
}
