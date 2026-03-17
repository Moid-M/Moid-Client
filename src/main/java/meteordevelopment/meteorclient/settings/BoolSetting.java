package meteordevelopment.meteorclient.settings;

public class BoolSetting extends Setting<Boolean> {
    private BoolSetting() { super("", "", false); }
    public void toggle() { value = !value; }

    public static class Builder extends AbstractBuilder<Builder, BoolSetting, Boolean> {
        public Builder() { instance = new BoolSetting(); instance.value = false; }
        public Builder defaultValue(boolean v) { instance.value = v; return this; }
        public BoolSetting build() { return (BoolSetting) instance; }
    }
}
