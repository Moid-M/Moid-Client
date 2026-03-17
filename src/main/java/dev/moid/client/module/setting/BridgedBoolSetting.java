package dev.moid.client.module.setting;

import dev.moid.client.MoidClient;
import java.lang.reflect.Method;

public class BridgedBoolSetting extends BooleanSetting {

    private final Object meteorSetting;

    public BridgedBoolSetting(String name, Object meteorSetting, boolean defaultValue) {
        super(name, defaultValue);
        this.meteorSetting = meteorSetting;
    }

    @Override
    public void toggle() {
        value = !value;
        writeBack();
    }

    @Override
    public void setValue(Boolean v) {
        value = v;
        writeBack();
    }

    private void writeBack() {
        try {
            Method set = meteorSetting.getClass().getMethod("set", Object.class);
            set.invoke(meteorSetting, value);
        } catch (Exception e) {
            MoidClient.LOGGER.error("Failed to write bool setting: " + e.getMessage());
        }
    }
}
