package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateRequest;
import com.hongik.graduationproject.domain.dto.video.VideoSummaryInitiateResponse;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hongik.graduationproject.eums.Platform.*;

@Service
@RequiredArgsConstructor
public class VideoSummaryService {
    private final MessageService messageService;
    private final VideoSummaryRepository videoSummaryRepository;

    public VideoSummaryInitiateResponse sendUrlToQueue(VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        String videoCode = generateVideoCode(videoSummaryInitiateRequest.getUrl());
        videoSummaryInitiateRequest.setVideoCode(videoCode);
        messageService.sendVideoUrlToQueue(videoSummaryInitiateRequest);
        return new VideoSummaryInitiateResponse(videoCode);
    }

//    public VideoSummaryDto getVideoByUuid(String uuid) {
//        Optional<VideoSummary> videoSummary = videoSummaryRepository.findByid/uuid);
//        return VideoSummaryDto.from(videoSummary.get());
//    }
//
//    public VideoSummaryStatusResponse getStatus(String uuid) {
//        return new VideoSummaryStatusResponse(videoSummaryRepository.existsById(uuid) ? "COMPLETE" : "PROCESSING");
//    }

    private String generateVideoCode(String url) {
        String platformName = getVideoPlatform(url);
        String videoId = getVideoId(url, platformName);
        return platformName.concat(videoId);
    }

    private String getVideoId(String url, String platformName) {
        String youtubeIdRegex = "(youtu.*be.*)\\/(watch\\?v=|embed\\/|v|shorts|)(.*?((?=[&#?])|$))";

        if (platformName.equals(YOUTUBE.getName())) {
            Pattern pattern = Pattern.compile(youtubeIdRegex);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(3);
            }
        } else if (platformName.equals(INSTAGRAM.getName())) {

        }
        // TODO : 예외처리 요망
        return "error";
    }

    private String getVideoPlatform(String url) {
        String youtubeValidationRegex = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|live\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
        String instagramValidationRegex = "(?:(?:http|https):\\/\\/)?(?:www.)?(?:instagram.com|instagr.am|instagr.com)\\/(\\w+)";
        if (url.matches(youtubeValidationRegex)) {
            return YOUTUBE.getName();
        } else if (url.matches(instagramValidationRegex)) {
            return INSTAGRAM.getName();
        } else {
            // TODO: 예외 처리 요망
            return "error";
        }
    }
}
