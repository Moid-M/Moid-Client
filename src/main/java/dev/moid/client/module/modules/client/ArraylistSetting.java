package dev.moid.client.module.modules.client;

import dev.moid.client.config.ClientConfig;
import dev.moid.client.module.Category;
import dev.moid.client.module.Module;
import dev.moid.client.module.setting.BooleanSetting;
import dev.moid.client.module.setting.SliderSetting;

public class ArraylistSetting extends Module {

    public static final BooleanSetting visible = new BooleanSetting("Visible", true);
    public static final SliderSetting  posX    = new SliderSetting("X Position", 5, 0, 500, 1);
    public static final SliderSetting  posY    = new SliderSetting("Y Position", 5, 0, 500, 1);

    public ArraylistSetting() {
        super("Arraylist", "Configure the on-screen module list", Category.CLIENT);
        addSetting(visible);
        addSetting(posX);
        addSetting(posY);
    }

    @Override
    public void onTick() {
        ClientConfig.showArraylist = visible.getValue();
        ClientConfig.arraylistX    = posX.getValue().intValue();
        ClientConfig.arraylistY    = posY.getValue().intValue();
    }
}
