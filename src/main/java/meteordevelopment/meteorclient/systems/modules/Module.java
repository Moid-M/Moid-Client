package meteordevelopment.meteorclient.systems.modules;

import meteordevelopment.meteorclient.settings.Settings;
import net.minecraft.client.MinecraftClient;

/**
 * Moid-Client stub for Meteor Client's Module class.
 * Addons extending this will load correctly without Meteor installed.
 */
public abstract class Module {

    public final String name;
    public final String description;
    public final Settings settings = new Settings();
    protected final MinecraftClient mc = MinecraftClient.getInstance();

    public Module(Category category, String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void onActivate() {}
    public void onDeactivate() {}
    public boolean isActive() { return false; }
    public void toggle() {}
    public String getName() { return name; }
    public String getDescription() { return description; }
}
