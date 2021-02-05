package site.liangbai.bettertitle.common.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import site.liangbai.bettertitle.BetterTitle;
import site.liangbai.bettertitle.util.TitleUtil;

import java.util.function.Supplier;

public final class PacketRequestChangeTitle {
    private final String title;

    public PacketRequestChangeTitle(PacketBuffer buffer) {
        String title = buffer.readString(Short.MAX_VALUE);

        if (title.isEmpty()) {
            this.title = null;

            return;
        }

        this.title = title;
    }

    public PacketRequestChangeTitle(String title) {
        this.title = title;
    }

    public void toBytes(PacketBuffer buf) {
        if (title == null || title.isEmpty()) {
            buf.writeString("");

            return;
        }

        buf.writeString(title);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) return;

        ctx.get().enqueueWork(() -> {

            BetterTitle.serverTitle = title;

            TitleUtil.updateTitle();
        });

        ctx.get().setPacketHandled(true);
    }
}
