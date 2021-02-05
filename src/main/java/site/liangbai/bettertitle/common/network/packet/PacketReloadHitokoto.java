package site.liangbai.bettertitle.common.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import site.liangbai.bettertitle.util.TitleUtil;

import java.util.function.Supplier;

public final class PacketReloadHitokoto {
    private final String type;

    public PacketReloadHitokoto(PacketBuffer buffer) {
        if (buffer.readString(Short.MAX_VALUE).isEmpty()) {
            type = null;

            return;
        }

        type = buffer.readString(Short.MAX_VALUE);
    }

    public PacketReloadHitokoto(String type) {
        this.type = type;
    }

    public void toBytes(PacketBuffer buf) {
        if (type == null || type.isEmpty()) {
            buf.writeString("");

            return;
        }

        buf.writeString(type);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) return;

        ctx.get().enqueueWork(() -> {
            TitleUtil.reloadHitokoto(type);

            TitleUtil.updateTitle();
        });

        ctx.get().setPacketHandled(true);
    }
}
