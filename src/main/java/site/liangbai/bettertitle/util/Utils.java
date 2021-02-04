package site.liangbai.bettertitle.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import site.liangbai.bettertitle.common.network.NetworkBetterTitle;
import site.liangbai.bettertitle.common.network.packet.PacketReloadHitokoto;
import site.liangbai.bettertitle.common.network.packet.PacketRequestChangeTitle;

import java.util.UUID;

public final class Utils {
    public static MinecraftServer server;

    private static PlayerEntity getPlayerEntityByUUID(UUID playerUniqueID) {
        for (ServerWorld world : server.getWorlds()) {
            PlayerEntity playerEntity = world.getPlayerByUuid(playerUniqueID);

            if (playerEntity != null) {
                return playerEntity;
            }
        }

        return null;
    }

    private static PlayerEntity getPlayerEntityByName(String playerName) {
        for (ServerWorld world : server.getWorlds()) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                ITextComponent name = player.getName();

                if (name.getString().equals(playerName)) {
                    return player;
                }
            }
        }

        return null;
    }

    public static void setPlayerClientTitleForPlayerEntity(PlayerEntity playerEntity, String title) {
        NetworkBetterTitle.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerEntity),
                new PacketRequestChangeTitle(title)
        );
    }

    public static void reloadPlayerClientHitokotoForPlayerEntity(PlayerEntity playerEntity, String type) {
        NetworkBetterTitle.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerEntity),
                new PacketReloadHitokoto(type)
        );
    }

    public static void setPlayerClientTitle(UUID playerUniqueID, String title) {
        PlayerEntity playerEntity = getPlayerEntityByUUID(playerUniqueID);

        if (playerEntity != null) {
            setPlayerClientTitleForPlayerEntity(playerEntity, title);
        }
    }

    public static void setPlayerClientTitle(String playerName, String title) {
        PlayerEntity playerEntity = getPlayerEntityByName(playerName);

        if (playerEntity != null) {
            setPlayerClientTitleForPlayerEntity(playerEntity, title);
        }
    }

    public static void reloadPlayerClientHitokoto(UUID playerUniqueID, String type) {
        PlayerEntity playerEntity = getPlayerEntityByUUID(playerUniqueID);

        if (playerEntity != null) {
            reloadPlayerClientHitokotoForPlayerEntity(playerEntity, type);
        }
    }

    public static void reloadPlayerClientHitokoto(String playerName, String type) {
        PlayerEntity playerEntity = getPlayerEntityByName(playerName);

        if (playerEntity != null) {
            reloadPlayerClientHitokotoForPlayerEntity(playerEntity, type);
        }
    }

    public static void reloadPlayerClientHitokoto(UUID playerUniqueID) {
        reloadPlayerClientHitokoto(playerUniqueID, null);
    }

    public static void reloadPlayerClientHitokoto(String playerName) {
        reloadPlayerClientHitokoto(playerName, null);
    }
}
