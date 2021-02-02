package site.liangbai.bettertitle.common.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import site.liangbai.bettertitle.BetterTitle;

import java.util.function.Supplier;

public final class PacketRequestChangeTitle {
    private final String title;

    public PacketRequestChangeTitle(PacketBuffer buffer) {
        title = buffer.readString(Short.MAX_VALUE);
    }

    public PacketRequestChangeTitle(String title) {
        this.title = title;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(this.title);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) return;

        ctx.get().enqueueWork(() -> {
            if (title == null || title.isEmpty()) return;

            BetterTitle.serverTitle = title;

            Minecraft.getInstance().setDefaultMinecraftTitle();
        });

        ctx.get().setPacketHandled(true);
    }
}
