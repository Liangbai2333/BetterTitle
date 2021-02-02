package site.liangbai.bettertitle.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import site.liangbai.bettertitle.common.network.NetworkSyncTitle;
import site.liangbai.bettertitle.common.network.packet.PacketRequestChangeTitle;

import java.util.UUID;

public final class Utils {
    public static MinecraftServer server;

    public static void setPlayerClientTitleForPlayerEntity(PlayerEntity playerEntity, String title) {
        NetworkSyncTitle.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerEntity),
                new PacketRequestChangeTitle(title)
        );
    }

    public static void setPlayerClientTitle(UUID playerUniqueID, String title) {
        for (ServerWorld world : server.getWorlds()) {
            PlayerEntity playerEntity = world.getPlayerByUuid(playerUniqueID);

            if (playerEntity != null) {
                setPlayerClientTitleForPlayerEntity(playerEntity, title);

                break;
            }
        }
    }
}
