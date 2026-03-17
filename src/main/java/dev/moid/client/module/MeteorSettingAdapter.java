package dev.moid.client.module;

import dev.moid.client.module.setting.Setting;
import meteordevelopment.meteorclient.settings.BoolSetting;

public class MeteorSettingAdapter extends Setting<Object> {

    private final meteordevelopment.meteorclient.settings.Setting<?> meteorSetting;

    public MeteorSettingAdapter(meteordevelopment.meteorclient.settings.Setting<?> meteorSetting) {
        super(meteorSetting.name, meteorSetting.get());
        this.meteorSetting = meteorSetting;
    }

    @Override
    public Object getValue() { return meteorSetting.get(); }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        ((meteordevelopment.meteorclient.settings.Setting<Object>) meteorSetting).set(value);
    }

    public boolean isBoolean() { return meteorSetting instanceof BoolSetting; }

    public void toggleBoolean() {
        if (meteorSetting instanceof BoolSetting bs) bs.toggle();
    }

    public meteordevelopment.meteorclient.settings.Setting<?> getMeteorSetting() {
        return meteorSetting;
    }
}
