package site.liangbai.bettertitle.util;

import net.minecraft.client.MainWindow;
import site.liangbai.bettertitle.BetterTitle;
import site.liangbai.bettertitle.config.Config;

import java.io.InputStream;

public final class IconUtil {
    public static void setDefaultIcon(MainWindow mainWindow, InputStream defaultIcon16X, InputStream defaultIcon32X) {
        Config config = BetterTitle.config;

        if (config == null || !config.getCustomIcon().isOpen()) {
            mainWindow.setWindowIcon(defaultIcon16X, defaultIcon32X);

            return;
        }

        Config.IconInfo iconInfo = config.getCustomIcon();

        InputStream iconStream16x = iconInfo.getIconStream16x() != null ? iconInfo.getIconStream16x() : defaultIcon16X;
        InputStream iconStream32x = iconInfo.getIconStream32x() != null ? iconInfo.getIconStream32x() : defaultIcon32X;

        mainWindow.setWindowIcon(iconStream16x, iconStream32x);
    }
}
