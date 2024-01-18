package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.MessageDto;
import com.hongik.graduationproject.domain.dto.video.VideoSummarizeRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryMessage;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
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

    public void sendMessage(MessageDto messageDto) {
        log.info("message sent: {}", messageDto.toString());
        rabbitTemplate.convertAndSend(exchangeName, urlRoutingKey, messageDto);
    }

    public void sendVideoUrlToQueue(VideoSummarizeRequest videoSummarizeRequest) {
        log.info("video url sent queue, url = {}, uuid = {}", videoSummarizeRequest.getUrl(), videoSummarizeRequest.getUuid());
        rabbitTemplate.convertAndSend(exchangeName, urlRoutingKey, videoSummarizeRequest);
    }

    @RabbitListener(queues = "${rabbitmq.summary.queue.name}")
    public void receiveVideoUrlFromQueue(VideoSummaryMessage videoSummaryMessage) {
        log.info("Received message: {}", videoSummaryMessage.toString());
        videoSummaryRepository.save(new VideoSummary(videoSummaryMessage));
    }
}
