package dev.moid.client.module;

import dev.moid.client.MoidClient;
import meteordevelopment.orbit.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MeteorModuleWrapper extends Module {

    private final Object instance;
    private Method onActivate;
    private Method onDeactivate;
    private Method onTick;

    public MeteorModuleWrapper(Object instance, Category addonCategory) {
        super(
            formatName(getFieldSafe(instance, "name",
                instance.getClass().getSimpleName())),
            getFieldSafe(instance, "description", "Addon module"),
            addonCategory
        );
        this.instance = instance;

        try { onActivate   = instance.getClass().getMethod("onActivate"); }
        catch (Exception ignored) {}
        try { onDeactivate = instance.getClass().getMethod("onDeactivate"); }
        catch (Exception ignored) {}
        try { onTick       = instance.getClass().getMethod("onTick"); }
        catch (Exception ignored) {}

        // Delegate settings bridging to dedicated class
        MeteorSettingsBridge.bridge(instance, this);
    }

    @Override
    public void onEnable() {
        try { if (onActivate != null) onActivate.invoke(instance); }
        catch (Exception ignored) {}
        EventBus.INSTANCE.subscribe(instance);
    }

    @Override
    public void onDisable() {
        try { if (onDeactivate != null) onDeactivate.invoke(instance); }
        catch (Exception ignored) {}
        EventBus.INSTANCE.unsubscribe(instance);
    }

    @Override
    public void onTick() {
        try { if (onTick != null) onTick.invoke(instance); }
        catch (Exception ignored) {}
    }

    private static String formatName(String raw) {
        if (raw == null || raw.isEmpty()) return raw;
        String[] words = raw.replace("-", " ").replace("_", " ").split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) sb.append(word.substring(1));
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    private static String getFieldSafe(Object obj, String fieldName, String fallback) {
        try {
            Class<?> c = obj.getClass();
            while (c != null) {
                try {
                    Field f = c.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    Object val = f.get(obj);
                    return val != null ? val.toString() : fallback;
                } catch (NoSuchFieldException ignored) {}
                c = c.getSuperclass();
            }
        } catch (Exception ignored) {}
        return fallback;
    }
}
