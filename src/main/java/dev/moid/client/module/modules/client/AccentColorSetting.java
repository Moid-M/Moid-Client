package dev.moid.client.module.modules.client;

import dev.moid.client.config.ClientConfig;
import dev.moid.client.module.Category;
import dev.moid.client.module.Module;
import dev.moid.client.module.setting.SliderSetting;

public class AccentColorSetting extends Module {

    public static final SliderSetting red   = new SliderSetting("Red",   0xD9, 0, 255, 1);
    public static final SliderSetting green = new SliderSetting("Green", 0x66, 0, 255, 1);
    public static final SliderSetting blue  = new SliderSetting("Blue",  0xFF, 0, 255, 1);

    public AccentColorSetting() {
        super("Accent Color", "Change the client accent color", Category.CLIENT);
        addSetting(red);
        addSetting(green);
        addSetting(blue);
    }

    @Override
    public void onTick() {
        int r = red.getValue().intValue();
        int g = green.getValue().intValue();
        int b = blue.getValue().intValue();
        ClientConfig.accentColor = 0xFF000000 | (r << 16) | (g << 8) | b;
    }
}
