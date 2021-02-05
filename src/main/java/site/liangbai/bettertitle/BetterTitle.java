package site.liangbai.bettertitle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import site.liangbai.bettertitle.common.network.NetworkBetterTitle;
import site.liangbai.bettertitle.config.Config;
import site.liangbai.bettertitle.util.TitleUtil;
import site.liangbai.bettertitle.util.Utils;
import site.liangbai.bettertitle.util.task.UpdateTitleTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@Mod(BetterTitle.MOD_ID)
public final class BetterTitle {
    public static final String MOD_ID = "bettertitle";

    private static final String LINE_STRING = "\n";

    private static final Logger LOGGER = LogManager.getLogger();

    public static String title;

    public static String serverTitle;

    public static Config config;

    public BetterTitle() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void initConfig() {
        LOGGER.info("[BetterTitle] Loading title.json file...");

        tryToLoadDefaultConfigJsonFromJson();

        LOGGER.info("[BetterTitle] Succeed in loading title.json file.");
    }

    private void setup(FMLCommonSetupEvent event) {
        if (FMLLoader.getDist().isClient()) {
            LOGGER.info("[BetterTitle] Pre loading title...");

            if (config != null && config.isOpenSyncFromServer()) {
                initNetwork();
            }

            tryToCompileTitle();

            startUpdateTitleTask();

            LOGGER.info("[BetterTitle] Loading title successful. Author: Liangbai.");

            return;
        }


        initNetwork();
    }

    private void initNetwork() {
        LOGGER.info("[BetterTitle] Register network for sync title.");

        Minecraft minecraft = Minecraft.getInstance();

        minecraft.enqueue(NetworkBetterTitle::init);
    }

    private static void tryToLoadDefaultConfigJsonFromJson() {
        Gson gson = new GsonBuilder().create();

        Path path = Paths.get("title.json");

        try {
            tryToCopyDefaultTitleJsonFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String json = linkStringForList(Files.readAllLines(path));

            config = gson.fromJson(json, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void tryToCompileTitle() {
        if (config != null) {
            if (config.getFixedTitle().isOpen()) {
                title = config.getFixedTitle().getTitle();
            } else if (config.getRandomTitle().isOpen()) {
                title = config.getRandomTitle().getRandomTitle();
            }
        }

        TitleUtil.updateTitle();
    }

    private static void tryToCopyDefaultTitleJsonFile(Path copyTo) throws IOException {
        if (!Files.exists(copyTo)) {
            Files.createFile(copyTo);

            ModFile modFile = FMLLoader.getLoadingModList().getModFileById(MOD_ID).getFile();

            Path resourcePath = modFile.getLocator().findPath(modFile, "title.json");

            if (!Files.exists(resourcePath)) {
                throw new IllegalStateException("can not found the default json file: " + "title.json" + ".");
            }

            String titleJson = linkStringForList(Files.readAllLines(resourcePath));

            Files.write(copyTo, titleJson.getBytes(StandardCharsets.UTF_8));
        }
    }

    private static String linkStringForList(List<String> stringList) {
        return String.join(LINE_STRING, stringList);
    }

    private static void startUpdateTitleTask() {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new UpdateTitleTask(TitleUtil::updateTitle), 500, 500);
    }

    public static void syncStartTime() {
        if (config != null) {
            Config.setStartTime(Config.getDateFormat().format(new Date()));
        }
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        Utils.server = event.getServer();
    }
}
