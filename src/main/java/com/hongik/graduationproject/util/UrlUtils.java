package com.hongik.graduationproject.util;

import com.hongik.graduationproject.eum.Platform;

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
        String idExtractRegex;
        int idIndex;

        switch (platform) {
            case YOUTUBE:
                idExtractRegex = YOUTUBE_ID_REGEX;
                idIndex = 3;
                break;
            case INSTAGRAM:
                idExtractRegex = INSTAGRAM_ID_REGEX;
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

    public static Platform getVideoPlatform(String url) {
        if (url.matches(YOUTUBE_VALIDATION_REGEX)) {
            return YOUTUBE;
        } else if (url.matches(INSTAGRAM_VALIDATION_REGEX)) {
            return INSTAGRAM;
        } else {
            // TODO: 예외 처리 요망
            throw new RuntimeException();
        }
    }
}
