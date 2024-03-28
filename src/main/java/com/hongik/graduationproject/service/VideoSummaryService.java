package com.hongik.graduationproject.service;

import com.hongik.graduationproject.domain.dto.video.*;
import com.hongik.graduationproject.domain.entity.VideoSummary;
import com.hongik.graduationproject.domain.entity.cache.VideoSummaryStatusCache;
import com.hongik.graduationproject.eum.Platform;
import com.hongik.graduationproject.repository.VideoSummaryRepository;
import com.hongik.graduationproject.repository.VideoSummaryStatusCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hongik.graduationproject.eum.Platform.*;

@Service
@RequiredArgsConstructor
public class VideoSummaryService {
    private final MessageService messageService;
    private final VideoSummaryRepository videoSummaryRepository;
    private final VideoSummaryStatusCacheRepository videoSummaryStatusCacheRepository;

    public VideoSummaryInitiateResponse initiateSummarizing(VideoSummaryInitiateRequest videoSummaryInitiateRequest) {
        Platform platform = getVideoPlatform(videoSummaryInitiateRequest.getUrl());
        String videoId = getVideoId(videoSummaryInitiateRequest.getUrl(), platform);

        String videoCode = platform.toString().concat("_").concat(videoId);

        if (!videoSummaryStatusCacheRepository.existsById(videoCode)) {
            if (videoSummaryRepository.existsByVideoCode(videoCode)) {
                VideoSummary videoSummary = videoSummaryRepository.findByVideoCode(videoCode).get();
                videoSummaryStatusCacheRepository.save(new VideoSummaryStatusCache(videoCode, videoSummary.getId(), "COMPLETE", videoSummary.getGeneratedCategory(), null));
            } else {
                messageService.sendVideoUrlToQueue(VideoSummaryInitiateMessage.builder()
                        .url(videoSummaryInitiateRequest.getUrl())
                        .platform(platform)
                        .videoCode(videoCode)
                        .build());

                videoSummaryStatusCacheRepository.save(new VideoSummaryStatusCache(videoCode, -1L, "PROCESSING", null, null));
            }
        }

        return new VideoSummaryInitiateResponse(videoCode);
    }

    public VideoSummaryDto getVideoSummaryById(Long videoSummaryId) {
        Optional<VideoSummary> videoSummary = videoSummaryRepository.findById(videoSummaryId);
        return VideoSummaryDto.from(videoSummary.get());
    }

    public VideoSummaryStatusResponse getStatus(String videoCode) {
        VideoSummaryStatusCache statusCache = videoSummaryStatusCacheRepository.findById(videoCode).get();
        return VideoSummaryStatusResponse.from(statusCache);
    }

//    private String generateVideoCode(String url) {
//        Platform platform = getVideoPlatform(url);
//        String videoId = getVideoId(url, platform);
//        return platform.toString().concat("_").concat(videoId);
//    }

    private String getVideoId(String url, Platform platform) {
        String idExtractRegex;
        int idIndex;

        switch (platform) {
            case YOUTUBE:
                idExtractRegex = "(youtu.*be.*)\\/(watch\\?v=|embed\\/|v|shorts|)(.*?((?=[&#?])|$))";
                idIndex = 3;
                break;
            case INSTAGRAM:
                idExtractRegex = "(?:https?:\\/\\/)?(?:www\\.)?instagram\\.com\\/?([a-zA-Z0-9\\.\\_\\-]+)?\\/([p]+)?([reel]+)?([tv]+)?([stories]+)?\\/([a-zA-Z0-9\\-\\_\\.]+)\\/?([0-9]+)?";
                idIndex = 6;
                break;
            default:
                throw new RuntimeException();
        }

        Pattern pattern = Pattern.compile(idExtractRegex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(idIndex);
        } else {
            // TODO : 예외처리 요망
            throw new RuntimeException();

        }
    }

    private Platform getVideoPlatform(String url) {
        String youtubeValidationRegex = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|live\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
        String instagramValidationRegex = "https?:\\/\\/(?:www.)?instagram.com\\/reels?\\/([^\\/?#&]+).*";
        if (url.matches(youtubeValidationRegex)) {
            return YOUTUBE;
        } else if (url.matches(instagramValidationRegex)) {
            return INSTAGRAM;
        } else {
            // TODO: 예외 처리 요망
            throw new RuntimeException();
        }
    }
}
