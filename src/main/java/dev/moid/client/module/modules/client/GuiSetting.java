package dev.moid.client.module.modules.client;

import dev.moid.client.config.ClientConfig;
import dev.moid.client.module.Category;
import dev.moid.client.module.Module;
import dev.moid.client.module.setting.BooleanSetting;
import dev.moid.client.module.setting.SliderSetting;

public class GuiSetting extends Module {

    public static final SliderSetting  scale        = new SliderSetting("GUI Scale", 0.8, 0.5, 1.5, 0.05);
    public static final BooleanSetting fade         = new BooleanSetting("Fade Animation", true);
    public static final BooleanSetting searchBottom = new BooleanSetting("Search at Bottom", false);
    public static final SliderSetting  panelWidth   = new SliderSetting("Panel Width", 130, 80, 250, 1);
    public static final SliderSetting  moduleHeight = new SliderSetting("Module Height", 20, 14, 32, 1);

    public GuiSetting() {
        super("GUI Settings", "Configure the click GUI appearance", Category.CLIENT);
        addSetting(scale);
        addSetting(fade);
        addSetting(searchBottom);
        addSetting(panelWidth);
        addSetting(moduleHeight);
    }

    @Override
    public void onTick() {
        ClientConfig.guiScale       = scale.getValue().floatValue();
        ClientConfig.fadeAnimation  = fade.getValue();
        ClientConfig.searchAtBottom = searchBottom.getValue();
        ClientConfig.panelWidth     = panelWidth.getValue().intValue();
        ClientConfig.moduleHeight   = moduleHeight.getValue().intValue();
    }
}
