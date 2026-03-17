package dev.moid.client.module.setting;

import dev.moid.client.MoidClient;
import java.lang.reflect.Method;

public class BridgedSliderSetting extends SliderSetting {

    private final Object meteorSetting;

    public BridgedSliderSetting(String name, Object meteorSetting, double defaultValue, double min, double max) {
        super(name, defaultValue, min, max, 0.01);
        this.meteorSetting = meteorSetting;
    }

    @Override
    public void setValue(Double v) {
        value = v;
        writeBack();
    }

    private void writeBack() {
        try {
            // Try int first then double
            try {
                Method set = meteorSetting.getClass().getMethod("set", Object.class);
                String type = meteorSetting.getClass().getSimpleName();
                if (type.equals("IntSetting")) {
                    set.invoke(meteorSetting, value.intValue());
                } else {
                    set.invoke(meteorSetting, value);
                }
            } catch (Exception e) {
                Method set = meteorSetting.getClass().getMethod("set", Object.class);
                set.invoke(meteorSetting, value);
            }
        } catch (Exception e) {
            MoidClient.LOGGER.error("Failed to write slider setting: " + e.getMessage());
        }
    }
}
