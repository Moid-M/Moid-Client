package dev.moid.client.module.modules.client;

import dev.moid.client.config.ClientConfig;
import dev.moid.client.module.Category;
import dev.moid.client.module.Module;
import dev.moid.client.module.setting.SliderSetting;

public class PanelColorSetting extends Module {

    public static final SliderSetting bgAlpha      = new SliderSetting("BG Alpha",     0xDD, 0, 255, 1);
    public static final SliderSetting headerR      = new SliderSetting("Header Red",   0x1a, 0, 255, 1);
    public static final SliderSetting headerG      = new SliderSetting("Header Green", 0x0a, 0, 255, 1);
    public static final SliderSetting headerB      = new SliderSetting("Header Blue",  0x2e, 0, 255, 1);

    public PanelColorSetting() {
        super("Panel Colors", "Customize panel and module row colors", Category.CLIENT);
        addSetting(bgAlpha);
        addSetting(headerR);
        addSetting(headerG);
        addSetting(headerB);
    }

    @Override
    public void onTick() {
        int alpha = bgAlpha.getValue().intValue();
        int r = headerR.getValue().intValue();
        int g = headerG.getValue().intValue();
        int b = headerB.getValue().intValue();
        ClientConfig.panelHeaderColor = 0xFF000000 | (r << 16) | (g << 8) | b;
        ClientConfig.panelHeaderHover = 0xFF000000 | (Math.min(r + 16, 255) << 16) | (Math.min(g + 16, 255) << 8) | Math.min(b + 16, 255);
        ClientConfig.panelBgColor     = (alpha << 24) | 0x000d0d1a;
        ClientConfig.moduleBgColor    = (alpha << 24) | 0x00120d1f;
        ClientConfig.moduleHoverColor = (alpha << 24) | 0x001f1035;
    }
}
