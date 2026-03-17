package dev.moid.client.module.setting;

public class BridgedStringSetting extends Setting<String> {

    private final Object meteorSetting;

    public BridgedStringSetting(String name, Object meteorSetting, String defaultValue) {
        super(name, defaultValue);
        this.meteorSetting = meteorSetting;
    }
}
