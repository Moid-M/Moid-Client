package dev.moid.client.module.modules;

import dev.moid.client.module.Category;
import dev.moid.client.module.Module;
import dev.moid.client.module.setting.BooleanSetting;
import dev.moid.client.module.setting.SliderSetting;

public class ClientSettings extends Module {

    public static final SliderSetting cornerRadius = new SliderSetting("Corner Radius", 0, 0, 10, 1);
    public static final BooleanSetting showHud = new BooleanSetting("Show HUD", true);
    public static final SliderSetting hudX = new SliderSetting("HUD X", 5, 0, 500, 1);
    public static final SliderSetting hudY = new SliderSetting("HUD Y", 5, 0, 500, 1);

    public ClientSettings() {
        super("Client Settings", "Configure Moid Client appearance", Category.CLIENT);
        addSetting(cornerRadius);
        addSetting(showHud);
        addSetting(hudX);
        addSetting(hudY);
    }
}
