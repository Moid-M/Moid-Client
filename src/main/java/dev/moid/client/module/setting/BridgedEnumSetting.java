package dev.moid.client.module.setting;

import dev.moid.client.MoidClient;
import java.lang.reflect.Method;

public class BridgedEnumSetting extends Setting<Object> {

    private final Object meteorSetting;
    private Object[] enumValues;

    public BridgedEnumSetting(String name, Object meteorSetting, Object defaultValue) {
        super(name, defaultValue);
        this.meteorSetting = meteorSetting;

        // Get all possible enum values
        try {
            if (defaultValue != null && defaultValue.getClass().isEnum()) {
                enumValues = defaultValue.getClass().getEnumConstants();
            }
        } catch (Exception ignored) {}
    }

    public Object[] getEnumValues() { return enumValues; }

    public void cycleNext() {
        if (enumValues == null || enumValues.length == 0) return;
        for (int i = 0; i < enumValues.length; i++) {
            if (enumValues[i].equals(value)) {
                value = enumValues[(i + 1) % enumValues.length];
                writeBack();
                return;
            }
        }
    }

    private void writeBack() {
        try {
            Method set = meteorSetting.getClass().getMethod("set", Object.class);
            set.invoke(meteorSetting, value);
        } catch (Exception e) {
            MoidClient.LOGGER.error("Failed to write enum setting: " + e.getMessage());
        }
    }
}
