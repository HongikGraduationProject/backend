package com.hongik.graduationproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlTest {
    public static String youtubeValidationRegex = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube(-nocookie)?\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|live\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
    public static String youtubeIdRegex = "(youtu.*be.*)\\/(watch\\?v=|embed\\/|v|shorts|)(.*?((?=[&#?])|$))";
    public static String instagramValidationRegex = "https?:\\/\\/(?:www.)?instagram.com\\/reels?\\/([^\\/?#&]+).*";
    public static String instagramIdRegex = "(?:https?:\\/\\/)?(?:www\\.)?instagram\\.com\\/?([a-zA-Z0-9\\.\\_\\-]+)?\\/([p]+)?([reel]+)?([tv]+)?([stories]+)?\\/([a-zA-Z0-9\\-\\_\\.]+)\\/?([0-9]+)?";
    public static String youtubeUrl = "https://youtube.com/shorts/6Kzbe73h6M4?si=95HwkzQd3WWmRfSO";
    public static String instagramUrl = "https://www.instagram.com/reels/C4cnSikMROE/";
    @Test
    void 유튜브_url() {
        Assertions.assertTrue(youtubeUrl.matches(youtubeValidationRegex));
        Assertions.assertFalse(instagramUrl.matches(youtubeValidationRegex));

        Pattern p = Pattern.compile(youtubeIdRegex);
        Matcher matcher = p.matcher(youtubeUrl);
        if (matcher.find()) {
            int groupCount = matcher.groupCount();
            for (int i = 0; i < groupCount; i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
        }
        Assertions.assertEquals("6Kzbe73h6M4", matcher.group(3));

    }

    @Test
    void 인스타그램_url_테스트 () {
        Assertions.assertTrue(instagramUrl.matches(instagramValidationRegex));

        Pattern pattern = Pattern.compile(instagramIdRegex);
        Matcher matcher = pattern.matcher(instagramUrl);
        if (matcher.find()) {
            System.out.println("Instagram URL detected");
            int groupCount = matcher.groupCount();
            for (int i = 0; i < groupCount; i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
        } else {
            System.out.println("Not an Instagram URL");
        }


    }

}
