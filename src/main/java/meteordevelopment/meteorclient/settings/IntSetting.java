package meteordevelopment.meteorclient.settings;

public class IntSetting extends Setting<Integer> {
    public int min, max;

    private IntSetting() { super("", "", 0); }

    public static class Builder extends AbstractBuilder<Builder, IntSetting, Integer> {
        public Builder() { instance = new IntSetting(); instance.value = 0; }
        public Builder min(int v) { ((IntSetting)instance).min = v; return this; }
        public Builder max(int v) { ((IntSetting)instance).max = v; return this; }
        public Builder range(int a, int b) { return min(a).max(b); }
        public Builder sliderMin(int v) { return this; }
        public Builder sliderMax(int v) { return this; }
        public Builder sliderRange(int a, int b) { return this; }
        public Builder noSlider() { return this; }
        public Builder defaultValue(int v) { instance.value = v; return this; }
        public IntSetting build() { return (IntSetting) instance; }
    }
}
