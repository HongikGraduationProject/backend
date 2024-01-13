package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.VideoSummarizeRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummarizeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {
    public VideoSummarizeResponse summarizeVideo(VideoSummarizeRequest videoSummarizeRequest){
        return new VideoSummarizeResponse(videoSummarizeRequest.getUrl());
    }
}
