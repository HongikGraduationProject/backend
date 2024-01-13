package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.MessageDto;
import com.hongik.graduationproject.domain.dto.video.VideoSummarizeRequest;
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

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageDto messageDto) {
        log.info("message sent: {}", messageDto.toString());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, messageDto);
    }

    public void sendVideoUrlToQueue(VideoSummarizeRequest videoSummarizeRequest) {
        log.info("message sent: {}", videoSummarizeRequest.toString());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, videoSummarizeRequest);
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveVideoUrlFromQueue(VideoSummarizeRequest videoSummarizeRequest) {
        log.info("Received message: {}", videoSummarizeRequest.toString());
    }
}
