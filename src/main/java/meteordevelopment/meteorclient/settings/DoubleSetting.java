package meteordevelopment.meteorclient.settings;

public class DoubleSetting extends Setting<Double> {
    public double min, max;

    private DoubleSetting() { super("", "", 0.0); }

    public static class Builder extends AbstractBuilder<Builder, DoubleSetting, Double> {
        public Builder() { instance = new DoubleSetting(); instance.value = 0.0; }
        public Builder min(double v) { ((DoubleSetting)instance).min = v; return this; }
        public Builder max(double v) { ((DoubleSetting)instance).max = v; return this; }
        public Builder range(double a, double b) { return min(a).max(b); }
        public Builder sliderMin(double v) { return this; }
        public Builder sliderMax(double v) { return this; }
        public Builder sliderRange(double a, double b) { return this; }
        public Builder noSlider() { return this; }
        public Builder defaultValue(double v) { instance.value = v; return this; }
        public DoubleSetting build() { return (DoubleSetting) instance; }
    }
}
