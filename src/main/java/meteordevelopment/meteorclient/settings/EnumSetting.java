package meteordevelopment.meteorclient.settings;

public class EnumSetting<T extends Enum<T>> extends Setting<T> {
    private EnumSetting() { super("", "", null); }

    public static class Builder<T extends Enum<T>> extends AbstractBuilder<Builder<T>, EnumSetting<T>, T> {
        public Builder() { instance = new EnumSetting<>(); }
        public Builder<T> defaultValue(T v) { instance.value = v; return this; }
        public EnumSetting<T> build() { return (EnumSetting<T>) instance; }
    }
}
