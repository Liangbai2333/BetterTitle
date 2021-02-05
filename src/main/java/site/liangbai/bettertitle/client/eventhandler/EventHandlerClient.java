package site.liangbai.bettertitle.client.eventhandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import site.liangbai.bettertitle.BetterTitle;
import site.liangbai.bettertitle.util.TitleUtil;

@Mod.EventBusSubscriber(Dist.CLIENT)
public final class EventHandlerClient {
    @SubscribeEvent
    public static void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        BetterTitle.serverTitle = null;

        TitleUtil.updateTitle();
    }
}
