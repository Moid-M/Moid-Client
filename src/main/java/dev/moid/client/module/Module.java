package dev.moid.client.module;

import dev.moid.client.module.setting.Setting;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled     = false;

    // Toggle flash animation — 0 to 1, fades out
    public float toggleFlash   = 0f;

    private final List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, String description, Category category) {
        this.name        = name;
        this.description = description;
        this.category    = category;
    }

    public void onEnable()  {}
    public void onDisable() {}
    public void onTick()    {}

    public void toggle() {
        enabled      = !enabled;
        toggleFlash  = 1f; // trigger flash
        if (enabled) onEnable();
        else onDisable();
    }

    // Call this every render frame to tick down the flash
    public void tickFlash() {
        if (toggleFlash > 0f) toggleFlash = Math.max(0f, toggleFlash - 0.08f);
    }

    public void addSetting(Setting<?> setting) { settings.add(setting); }
    public List<Setting<?>> getSettings()      { return settings; }
    public String getName()                    { return name; }
    public String getDescription()             { return description; }
    public Category getCategory()              { return category; }
    public boolean isEnabled()                 { return enabled; }
}
