package com.hongik.graduationproject.util;

import com.hongik.graduationproject.eum.Platform;
import com.hongik.graduationproject.exception.AppException;
import com.hongik.graduationproject.exception.ErrorCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hongik.graduationproject.eum.Platform.INSTAGRAM;
import static com.hongik.graduationproject.eum.Platform.YOUTUBE;

public class UrlUtils {
    private static final String INSTAGRAM_ID_REGEX = "(?:https?:\\/\\/)?(?:www\\.)?instagram\\.com\\/?([a-zA-Z0-9\\.\\_\\-]+)?\\/([p]+)?([reel]+)?([tv]+)?([stories]+)?\\/([a-zA-Z0-9\\-\\_\\.]+)\\/?([0-9]+)?";
    private static final String YOUTUBE_ID_REGEX = "(youtu.*be.*)\\/(watch\\?v=|embed\\/|v|shorts|)(.*?((?=[&#?])|$))";
    private static final String INSTAGRAM_VALIDATION_REGEX = "https?:\\/\\/(?:www.)?instagram.com\\/reels?\\/([^\\/?#&]+).*";
    private static final String YOUTUBE_VALIDATION_REGEX = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|live\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";

    public static String getVideoId(String url, Platform platform) {
        switch (platform) {
            case YOUTUBE:
                return extractYoutubeId(url);
            case INSTAGRAM:
                return extractInstagramId(url);
        }
        throw new AppException(ErrorCode.FAILED_TO_EXTRACT_EXTRACT_ID);
    }

    public static Platform getVideoPlatform(String url) {
        if (url.matches(YOUTUBE_VALIDATION_REGEX)) {
            return YOUTUBE;
        } else if (url.matches(INSTAGRAM_VALIDATION_REGEX)) {
            return INSTAGRAM;
        } else {
            throw new AppException(ErrorCode.INVALID_VIDEO_URL);
        }
    }

    private static String extractYoutubeId(String url) {
        Pattern pattern = Pattern.compile(YOUTUBE_ID_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(3);
        } else {
            throw new AppException(ErrorCode.FAILED_TO_EXTRACT_EXTRACT_ID);
        }
    }

    private static String extractInstagramId(String url) {
        Pattern pattern = Pattern.compile(INSTAGRAM_ID_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(6);
        } else {
            throw new AppException(ErrorCode.FAILED_TO_EXTRACT_EXTRACT_ID);
        }
    }
}
