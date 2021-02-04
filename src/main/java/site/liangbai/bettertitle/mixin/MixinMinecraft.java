package site.liangbai.bettertitle.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.SharedConstants;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.versions.forge.ForgeVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import site.liangbai.bettertitle.BetterTitle;
import site.liangbai.bettertitle.config.Config;
import site.liangbai.bettertitle.util.TitleUtil;

import java.util.Date;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    private IntegratedServer integratedServer;

    @Shadow
    private ServerData currentServerData;

    @Shadow
    public abstract boolean isConnectedToRealms();

    @Inject(
            method = "getWindowTitle",
            at = @At("HEAD"),
            cancellable = true
    )
    private void inject_getWindowTitle(CallbackInfoReturnable<String> callbackInfoReturnable) {
        if (BetterTitle.serverTitle != null && !BetterTitle.serverTitle.isEmpty()) {
            callbackInfoReturnable.setReturnValue(applyTitle(BetterTitle.serverTitle));

            return;
        }

        if (BetterTitle.title != null && !BetterTitle.title.isEmpty()) {
            callbackInfoReturnable.setReturnValue(applyTitle(BetterTitle.title));
        }
    }

    private String applyTitle(String title) {
        if (title == null || title.isEmpty()) return null;

        String gameType;

        if (this.integratedServer != null && !this.integratedServer.getPublic()) {
            gameType = I18n.format("title.singleplayer");
        } else if (this.isConnectedToRealms()) {
            gameType = I18n.format("title.multiplayer.realms");
        } else if (this.integratedServer == null && (this.currentServerData == null || !this.currentServerData.isOnLAN())) {
            gameType = I18n.format("title.multiplayer.other");
        } else {
            gameType = I18n.format("title.multiplayer.lan");
        }

        return title
                .replace("%mod_count%", Integer.toString(FMLLoader.getLoadingModList().getMods().size()))
                .replace("%mc_version%", SharedConstants.getVersion().getName())
                .replace("%forge_version%", ForgeVersion.getVersion())
                .replace("%time%", Config.getDateFormat().format(new Date()))
                .replace("%play_type%", gameType)
                .replace("%start_time%", Config.getStartTime())
                .replace("%hitokoto%", TitleUtil.getHitokotoSentence())
                .replace("%hitokoto_from%", TitleUtil.getHitokotoFrom())
                .replace("%hitokoto_type%", TitleUtil.getHitokotoType());
    }
}
