package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.VideoSummarizeRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummarizeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class VideoService {
    private final MessageService messageService;
    public VideoSummarizeResponse sendUrlToQueue(VideoSummarizeRequest videoSummarizeRequest){
        String uuid = UUID.randomUUID().toString();
        videoSummarizeRequest.setUuid(uuid);
        messageService.sendVideoUrlToQueue(videoSummarizeRequest);
        return new VideoSummarizeResponse(uuid);
    }
}
