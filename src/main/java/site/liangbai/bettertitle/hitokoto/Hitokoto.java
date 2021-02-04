package site.liangbai.bettertitle.hitokoto;

public final class Hitokoto {
    private final String sentence;

    private final String from;

    private final String type;

    public String getFrom() {
        return from;
    }

    public String getSentence() {
        return sentence;
    }

    public Hitokoto(String sentence, String from, String type) {
        this.sentence = sentence;
        this.from = from;
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
