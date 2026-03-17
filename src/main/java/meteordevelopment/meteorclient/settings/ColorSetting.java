package meteordevelopment.meteorclient.settings;

import meteordevelopment.meteorclient.utils.render.color.SettingColor;

public class ColorSetting extends Setting<SettingColor> {
    private ColorSetting() { super("", "", new SettingColor(255,255,255,255)); }

    public static class Builder extends AbstractBuilder<Builder, ColorSetting, SettingColor> {
        public Builder() { instance = new ColorSetting(); }
        public Builder defaultValue(SettingColor v) { instance.value = v; return this; }
        public ColorSetting build() { return (ColorSetting) instance; }
    }
}
