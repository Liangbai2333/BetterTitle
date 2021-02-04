package site.liangbai.bettertitle.util;

import java.util.regex.Pattern;

public final class UrlUtil {
    private static final Pattern urlPattern = Pattern.compile("http[s]?://.+[.].+[/]?");

    public static boolean isUrl(String url) {
        return urlPattern.matcher(url).matches();
    }
}
