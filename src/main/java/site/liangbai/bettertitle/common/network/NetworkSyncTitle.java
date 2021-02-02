package site.liangbai.bettertitle.common.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import site.liangbai.bettertitle.common.network.packet.PacketRequestChangeTitle;

public final class NetworkSyncTitle {
    public static SimpleChannel INSTANCE;
    private static final String VERSION = "1.0-SNAPSHOT";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("synctitle", "synctitle_networking"),
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
    }
}
