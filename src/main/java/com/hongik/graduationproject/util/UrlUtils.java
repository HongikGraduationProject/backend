package com.hongik.graduationproject.util;

import com.hongik.graduationproject.eum.Platform;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hongik.graduationproject.eum.Platform.INSTAGRAM;
import static com.hongik.graduationproject.eum.Platform.YOUTUBE;

public class UrlUtils {
    public static String getVideoId(String url, Platform platform) {
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

    public static Platform getVideoPlatform(String url) {
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
