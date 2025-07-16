package jyaml;

public final class YamlNull {
    public static final YamlNull INSTANCE = new YamlNull();

    private YamlNull() {}

    @Override
    public String toString() {
        return "null";
    }
}
