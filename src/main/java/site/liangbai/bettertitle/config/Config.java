package site.liangbai.bettertitle.config;

import site.liangbai.bettertitle.BetterTitle;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public final class Config {
    private FixedTitle fixedTitle;
    
    private RandomTitle randomTitle;
    
    private String dateFormat;
    
    private static SimpleDateFormat simpleDateFormat;

    private static String startTime;

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
}
