package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.Coordinate;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateMessage;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryMessage;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import com.hongik.graduationproject.enums.MainCategory;
import com.hongik.graduationproject.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
	private final VideoSummaryStatusCacheRepository videoSummaryStatusCacheRepository;
	private final VideoSummaryRepository videoSummaryRepository;
	private final GeocodeService geocodeService;

	public void sendVideoUrlToQueue(VideoSummaryInitiateMessage videoSummaryInitiateMessage) {
		log.info("Sent url: {}, videoCode: {}", videoSummaryInitiateMessage.getUrl(),
			videoSummaryInitiateMessage.getVideoCode());
		rabbitTemplate.convertAndSend(exchangeName, urlRoutingKey, videoSummaryInitiateMessage);
	}

	@RabbitListener(queues = "${rabbitmq.summary.queue.name}")
	@Transactional
	public void receiveVideoUrlFromQueue(VideoSummaryMessage videoSummaryMessage) {
		log.info("Received message: {}", videoSummaryMessage.toString());

		VideoSummary savedVideoSummary = videoSummaryRepository.save(VideoSummary.of(videoSummaryMessage));

		Coordinate coordinate = null;
		if (savedVideoSummary.getAddress() != null && !savedVideoSummary.getAddress().isEmpty()) {
			coordinate = geocodeService.getCoordinateByAddress(savedVideoSummary.getAddress());

			savedVideoSummary.updateLatitude(coordinate.getLatitude());
			savedVideoSummary.updateLongitude(coordinate.getLongitude());
		}

		updateStatusCache(videoSummaryMessage, savedVideoSummary);
	}

	private void updateStatusCache(VideoSummaryMessage videoSummaryMessage, VideoSummary savedVideoSummary) {
		List<VideoSummaryStatusCache> statusCacheList = videoSummaryStatusCacheRepository.findAllByVideoCode(
			videoSummaryMessage.getVideoCode());

		statusCacheList.forEach(cache -> {
			cache.updateStatus("COMPLETE");
			cache.updateVideoSummaryId(savedVideoSummary.getId());
			cache.updateGeneratedMainCategory(MainCategory.find(videoSummaryMessage.getGeneratedMainCategoryName()));
		});

		videoSummaryStatusCacheRepository.saveAll(statusCacheList);
	}

}