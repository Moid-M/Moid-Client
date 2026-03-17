package dev.moid.client.gui;

import dev.moid.client.MoidClient;
import dev.moid.client.config.ClientConfig;
import dev.moid.client.module.Module;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class HudRenderer {

    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (!ClientConfig.showArraylist) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            int y = ClientConfig.arraylistY;
            for (Module module : MoidClient.moduleManager.getModules()) {
                if (module.isEnabled()) {
                    drawContext.drawTextWithShadow(
                        client.textRenderer,
                        module.getName(),
                        ClientConfig.arraylistX, y,
                        ClientConfig.arraylistColor
                    );
                    y += 12;
                }
            }
        });
    }
}
