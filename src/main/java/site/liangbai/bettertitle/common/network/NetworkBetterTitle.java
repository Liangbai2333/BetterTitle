package site.liangbai.bettertitle.common.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import site.liangbai.bettertitle.BetterTitle;
import site.liangbai.bettertitle.common.network.packet.PacketReloadHitokoto;
import site.liangbai.bettertitle.common.network.packet.PacketRequestChangeTitle;

public final class NetworkBetterTitle {
    public static SimpleChannel INSTANCE;
    private static final String VERSION = "1.3";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(BetterTitle.MOD_ID, BetterTitle.MOD_ID + "_networking"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );

        registerMessage();
    }

    private static void registerMessage() {
        INSTANCE.registerMessage(
                nextID(),
                PacketRequestChangeTitle.class,
                PacketRequestChangeTitle::toBytes,
                PacketRequestChangeTitle::new,
                PacketRequestChangeTitle::handle
        );

        INSTANCE.registerMessage(
                nextID(),
                PacketReloadHitokoto.class,
                PacketReloadHitokoto::toBytes,
                PacketReloadHitokoto::new,
                PacketReloadHitokoto::handle
        );
    }
}
