package site.liangbai.bettertitle.util;

import net.minecraft.client.Minecraft;
import site.liangbai.bettertitle.hitokoto.Hitokoto;

import java.io.IOException;

public final class TitleUtil {
    private static Hitokoto hitokoto;

    public static void reloadHitokoto(String type) {
        try {
            String json = HitokotoUtil.randomHitokoto(type);

            if (json == null) return;

            hitokoto = HitokotoUtil.parseHitokotoJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Hitokoto getHitokoto() {
        if (hitokoto == null) {
            reloadHitokoto(null);

            if (hitokoto == null) {
                hitokoto = new Hitokoto("", "", "");
            }
        }

        return hitokoto;
    }

    public static String getHitokotoSentence() {
        return getHitokoto().getSentence();
    }

    public static String getHitokotoFrom() {
        return getHitokoto().getFrom();
    }

    public static String getHitokotoType() {
        return getHitokoto().getType();
    }

    public static void updateTitle() {
        Minecraft minecraft = Minecraft.getInstance();

        minecraft.enqueue(minecraft::setDefaultMinecraftTitle);
    }
}
