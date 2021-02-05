package site.liangbai.bettertitle.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import site.liangbai.bettertitle.hitokoto.Hitokoto;
import site.liangbai.bettertitle.util.network.HttpRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class HitokotoUtil {
    public static final Map<String, String> sentenceType = new HashMap<>();
    public static final Map<String, String> againstSentenceType = new HashMap<>();

    static {
        registerSentenceType("动画", "a");
        registerSentenceType("漫画", "b");
        registerSentenceType("游戏", "c");
        registerSentenceType("文学", "d");
        registerSentenceType("原创", "e");
        registerSentenceType("网络", "f");
        registerSentenceType("其他", "g");
        registerSentenceType("影视", "h");
        registerSentenceType("诗词", "i");
        registerSentenceType("网抑云", "j");
        registerSentenceType("网易云", "j");
        registerSentenceType("哲学", "k");
        registerSentenceType("抖机灵", "l");
    }

    public static void registerSentenceType(String type, String realType) {
        sentenceType.put(type, realType);
        againstSentenceType.put(realType, type);
    }

    public static Hitokoto parseHitokotoJson(String json) {
        JsonParser jsonParser = new JsonParser();

        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        String hitokoto = jsonObject.get("hitokoto").getAsString();

        String from;

        if (jsonObject.has("from_who") && !jsonObject.get("from_who").isJsonNull()) {
            from = jsonObject.get("from_who").getAsString();
        } else if (jsonObject.has("from") && !jsonObject.get("from").isJsonNull()) {
            from = jsonObject.get("from").getAsString();
        } else {
            from = jsonObject.get("creator").getAsString();
        }

        String type = "None";

        if (jsonObject.has("type") && !jsonObject.get("type").isJsonNull()) {
            String typeJson = jsonObject.get("type").getAsString();

            if (againstSentenceType.containsKey(typeJson)) {
                type = againstSentenceType.get(typeJson);
            }
        }

        return new Hitokoto(hitokoto, from, type);
    }

    public static String randomHitokoto(String type) throws IOException {
        String url = "https://v1.hitokoto.cn/";

        if (type != null) {
            url = url + "?c=";

            if (againstSentenceType.containsKey(type)) {
                url = url + type;
            } else if (sentenceType.containsKey(type)) {
                url = url + sentenceType.get(type);
            } else {
                return null;
            }
        }

        HttpRequest httpRequest = new HttpRequest(url);

        httpRequest.putRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
        httpRequest.putRequestProperty("Cookie", "none");

        return httpRequest.sendRequestAndReturn();
    }
}
