package dev.moid.client;

import dev.moid.client.event.EventDispatcher;
import dev.moid.client.gui.ClickGui;
import dev.moid.client.gui.HudRenderer;
import dev.moid.client.manager.AddonLoader;
import dev.moid.client.manager.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoidClient implements ClientModInitializer {

    public static final String MOD_NAME    = "Moid-Client";
    public static final String MOD_VERSION = "1.0.0";
    public static final Logger LOGGER      = LoggerFactory.getLogger(MOD_NAME);

    public static ModuleManager moduleManager;
    public static AddonLoader addonLoader;
    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Moid-Client starting up...");

        moduleManager = new ModuleManager();
        moduleManager.init();

        addonLoader = new AddonLoader();
        addonLoader.loadAll();

        LOGGER.info("Registered categories:");
        moduleManager.getModules().stream()
            .map(m -> m.getCategory().name())
            .distinct()
            .forEach(cat -> LOGGER.info("  - " + cat));

        // Register event dispatcher — fires Meteor events via Fabric hooks
        EventDispatcher.register();

        HudRenderer.register();

        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.moid-client.gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "Moid Client"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (guiKey.wasPressed() && client.player != null) {
                client.setScreen(new ClickGui());
            }
            moduleManager.tickAll();
        });

        LOGGER.info("Moid-Client loaded successfully!");
    }
}
