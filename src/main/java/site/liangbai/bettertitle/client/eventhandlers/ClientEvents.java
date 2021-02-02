package site.liangbai.bettertitle.client.eventhandlers;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import site.liangbai.bettertitle.BetterTitle;

@Mod.EventBusSubscriber(Dist.CLIENT)
public final class ClientEvents {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft.getInstance().setDefaultMinecraftTitle();
    }

    @SubscribeEvent
    public static void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        BetterTitle.serverTitle = null;

        Minecraft.getInstance().setDefaultMinecraftTitle();
    }
}
