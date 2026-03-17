package dev.moid.client.module.setting;

public class SliderSetting extends Setting<Double> {
    private final double min, max, step;

    public SliderSetting(String name, double defaultValue, double min, double max, double step) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getStep() { return step; }
}
