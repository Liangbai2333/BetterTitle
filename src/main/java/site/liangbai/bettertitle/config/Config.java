package site.liangbai.bettertitle.config;

import org.apache.logging.log4j.LogManager;
import site.liangbai.bettertitle.BetterTitle;
import site.liangbai.bettertitle.util.UrlUtil;
import site.liangbai.bettertitle.util.network.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public final class Config {
    private boolean openSyncFromServer;

    private FixedTitle fixedTitle;
    
    private RandomTitle randomTitle;

    private IconInfo customIcon;
    
    private String dateFormat;
    
    private static SimpleDateFormat simpleDateFormat;

    private static String startTime;

    public boolean isOpenSyncFromServer() {
        return openSyncFromServer;
    }

    public IconInfo getCustomIcon() {
        return customIcon;
    }

    public static String getStartTime() {
        return startTime;
    }

    public static void setStartTime(String startTime) {
        Config.startTime = startTime;
    }

    public FixedTitle getFixedTitle() {
        return fixedTitle;
    }

    public RandomTitle getRandomTitle() {
        return randomTitle;
    }

    public static SimpleDateFormat getDateFormat() {
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(BetterTitle.config.dateFormat);
        }

        return simpleDateFormat;
    }

    public static final class FixedTitle {
        private boolean open;
        
        private String title;

        public boolean isOpen() {
            return open;
        }

        public String getTitle() {
            return title;
        }
    }
    
    public static final class RandomTitle {
        private boolean open;
        
        private List<String> titles;

        public boolean isOpen() {
            return open;
        }
        
        public String getRandomTitle() {
            if (titles.size() < 1 || !isOpen()) return null;

            Random random = new Random();
            
            return titles.get(random.nextInt(titles.size()));
        }
    }

    public static final class IconInfo {
        private boolean open;

        private IconLocationInfo icon;

        private InputStream iconStream16x;
        private InputStream iconStream32x;

        public static final class IconLocationInfo {
            private String icon16x;

            private String icon32x;
        }

        public boolean isOpen() {
            return open;
        }

        private InputStream getInputStreamByUrl(String url) {
            if (UrlUtil.isUrl(url)) {
                try {
                    HttpRequest httpRequest = new HttpRequest(url);

                    return httpRequest.getUrlConnection().getInputStream();
                } catch (IOException e) {
                    LogManager.getLogger().error("can not get icon from url: " + url + ", error: " + e.getLocalizedMessage());

                    return null;
                }
            } else {
                Path path = Paths.get(url);

                if (!Files.exists(path)) return null;

                try {
                    return Files.newInputStream(path);
                } catch (IOException e) {
                    LogManager.getLogger().error("can not get icon from file: " + url + ", error: " + e.getLocalizedMessage());

                    return null;
                }
            }
        }

        public InputStream getIconStream16x() {
            if (iconStream16x == null) {
                iconStream16x = getInputStreamByUrl(icon.icon16x);
            }

            return iconStream16x;
        }

        public InputStream getIconStream32x() {
            if (iconStream32x == null) {
                iconStream32x = getInputStreamByUrl(icon.icon32x);
            }

            return iconStream32x;
        }
    }
}
