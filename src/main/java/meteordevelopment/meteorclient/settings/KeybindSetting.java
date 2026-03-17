package meteordevelopment.meteorclient.settings;

public class KeybindSetting extends Setting<Integer> {
    private KeybindSetting() { super("", "", -1); }

    public static class Builder extends AbstractBuilder<Builder, KeybindSetting, Integer> {
        public Builder() { instance = new KeybindSetting(); instance.value = -1; }
        public Builder defaultValue(int v) { instance.value = v; return this; }
        public KeybindSetting build() { return (KeybindSetting) instance; }
    }
}
