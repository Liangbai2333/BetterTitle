package site.liangbai.bettertitle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import site.liangbai.bettertitle.common.network.NetworkSyncTitle;
import site.liangbai.bettertitle.config.Config;
import site.liangbai.bettertitle.util.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Mod(BetterTitle.MOD_ID)
public final class BetterTitle {
    public static final String MOD_ID = "bettertitle";

    private static final String LINE_STRING = "\n";

    public static String title;

    public static String serverTitle;

    public static Config config;

    private static final Logger LOGGER = LogManager.getLogger();

    public BetterTitle() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkSyncTitle::init);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Pre loading title successful. Author: Liangbai");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Path path = Paths.get("title.json");

        if (!Files.exists(path)) {
            try {
                Files.createFile(path);

                String json = "{\n" +
                        "  \"fixedTitle\": {\n" +
                        "    \"open\": true,\n" +
                        "    \"title\": \"Minecraft* - %mc_version% - Forge - %forge_version% - modCount: %mod_count% - time: %time% - %play_type% startTime: %start_time%\"\n" +
                        "  },\n" +
                        "\n" +
                        "  \"randomTitle\": {\n" +
                        "    \"open\": false,\n" +
                        "    \"titles\": [\n" +
                        "      \"Minecraft* - %mc_version% - Forge - %forge_version% - modCount: %mod_count% - time: %time% - %play_type% startTime: %start_time%\"\n" +
                        "    ]\n" +
                        "  },\n" +
                        "\n" +
                        "  \"dateFormat\": \"yyyy年MM月dd日 HH:mm:ss\"\n" +
                        "}";

                Files.write(path, json.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String json = String.join(LINE_STRING, Files.readAllLines(path));

            config = gson.fromJson(json, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (config != null) {
            if (config.getFixedTitle().isOpen()) {
                title = config.getFixedTitle().getTitle();
            } else if (config.getRandomTitle().isOpen()) {
                title = config.getRandomTitle().getRandomTitle();
            }
        }

        Config.setStartTime(Config.getDateFormat().format(new Date()));

        Minecraft.getInstance().setDefaultMinecraftTitle();

        LOGGER.info("Loading title successful. Author: Liangbai");
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        Utils.server = event.getServer();
    }
}
