package dev.moid.client.module;

import dev.moid.client.MoidClient;
import dev.moid.client.module.setting.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class MeteorSettingsBridge {

    public static void bridge(Object instance, Module target) {
        try {
            Class<?> c = instance.getClass();
            while (c != null) {
                try {
                    Field settingsField = c.getDeclaredField("settings");
                    settingsField.setAccessible(true);
                    Object meteorSettings = settingsField.get(instance);
                    if (meteorSettings == null) return;

                    Method getGroups = meteorSettings.getClass().getMethod("getGroups");
                    List<?> groups   = (List<?>) getGroups.invoke(meteorSettings);

                    MoidClient.LOGGER.info("Bridging " + groups.size()
                        + " setting groups for " + target.getName());

                    for (Object group : groups) {
                        Method getSettings = group.getClass().getMethod("getSettings");
                        List<?> settings   = (List<?>) getSettings.invoke(group);
                        for (Object setting : settings) {
                            bridgeSetting(setting, target);
                        }
                    }
                    return;
                } catch (NoSuchFieldException ignored) {}
                c = c.getSuperclass();
            }
        } catch (Exception e) {
            MoidClient.LOGGER.error("Failed to bridge settings for "
                + target.getName() + ": " + e.getMessage());
        }
    }

    private static void bridgeSetting(Object meteorSetting, Module target) {
        try {
            String name  = (String) meteorSetting.getClass()
                .getMethod("getName").invoke(meteorSetting);
            Object value = meteorSetting.getClass()
                .getMethod("get").invoke(meteorSetting);
            String type  = meteorSetting.getClass().getSimpleName();

            MoidClient.LOGGER.info("  Setting: " + name
                + " type=" + type + " value=" + value);

            switch (type) {
                case "BoolSetting" -> target.addSetting(
                    new BridgedBoolSetting(name, meteorSetting, (Boolean) value));

                case "IntSetting" -> target.addSetting(
                    new BridgedSliderSetting(name, meteorSetting,
                        ((Number) value).doubleValue(),
                        getDouble(meteorSetting, "min", 0),
                        getDouble(meteorSetting, "max", 100)));

                case "DoubleSetting" -> target.addSetting(
                    new BridgedSliderSetting(name, meteorSetting,
                        ((Number) value).doubleValue(),
                        getDouble(meteorSetting, "min", 0),
                        getDouble(meteorSetting, "max", 10)));

                case "EnumSetting" -> target.addSetting(
                    new BridgedEnumSetting(name, meteorSetting, value));

                case "StringSetting" -> target.addSetting(
                    new BridgedStringSetting(name, meteorSetting,
                        value != null ? value.toString() : ""));

                default -> target.addSetting(
                    new BridgedStringSetting(name, meteorSetting,
                        value != null ? value.toString() : "??"));
            }
        } catch (Exception e) {
            MoidClient.LOGGER.error("  Failed to bridge setting: " + e.getMessage());
        }
    }

    private static double getDouble(Object obj, String fieldName, double fallback) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return ((Number) f.get(obj)).doubleValue();
        } catch (Exception ignored) {}
        return fallback;
    }
}
