package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.VideoSummarizeRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummarizeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final MessageService messageService;
    public VideoSummarizeResponse summarizeVideo(VideoSummarizeRequest videoSummarizeRequest){
        messageService.sendVideoUrlToQueue(videoSummarizeRequest);
        return new VideoSummarizeResponse("in queue");
    }
}
