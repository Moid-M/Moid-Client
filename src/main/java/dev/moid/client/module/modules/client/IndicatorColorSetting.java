package dev.moid.client.module.modules.client;

import dev.moid.client.config.ClientConfig;
import dev.moid.client.module.Category;
import dev.moid.client.module.Module;
import dev.moid.client.module.setting.SliderSetting;

public class IndicatorColorSetting extends Module {

    public static final SliderSetting red   = new SliderSetting("Red",   0xFF, 0, 255, 1);
    public static final SliderSetting green = new SliderSetting("Green", 0x69, 0, 255, 1);
    public static final SliderSetting blue  = new SliderSetting("Blue",  0xD9, 0, 255, 1);

    public IndicatorColorSetting() {
        super("Indicator Color", "Change the enabled module indicator color", Category.CLIENT);
        addSetting(red);
        addSetting(green);
        addSetting(blue);
    }

    @Override
    public void onTick() {
        int r = red.getValue().intValue();
        int g = green.getValue().intValue();
        int b = blue.getValue().intValue();
        ClientConfig.enabledDotColor  = 0xFF000000 | (r << 16) | (g << 8) | b;
        ClientConfig.enabledTextColor = 0xFF000000 | (r << 16) | (g << 8) | b;
    }
}
