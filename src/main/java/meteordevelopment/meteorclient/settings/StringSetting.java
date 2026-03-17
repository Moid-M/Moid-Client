package meteordevelopment.meteorclient.settings;

public class StringSetting extends Setting<String> {
    private StringSetting() { super("", "", ""); }

    public static class Builder extends AbstractBuilder<Builder, StringSetting, String> {
        public Builder() { instance = new StringSetting(); instance.value = ""; }
        public Builder defaultValue(String v) { instance.value = v; return this; }
        public StringSetting build() { return (StringSetting) instance; }
    }
}
