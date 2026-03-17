package dev.moid.client.event;

import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventBus;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;

public class EventDispatcher {

    public static void register() {

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null)
                EventBus.INSTANCE.post(new TickEvent.Pre());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null)
                EventBus.INSTANCE.post(new TickEvent.Post());
        });

        // Use matrixStack from context, tickDelta from MinecraftClient
        WorldRenderEvents.LAST.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            float tickDelta = client.getRenderTickCounter().getTickDelta(true);
            EventBus.INSTANCE.post(new Render3DEvent(
                context.matrixStack(),
                tickDelta
            ));
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            float tickDelta = client.getRenderTickCounter().getTickDelta(true);
            EventBus.INSTANCE.post(new Render2DEvent(
                drawContext,
                client.getWindow().getScaledWidth(),
                client.getWindow().getScaledHeight(),
                tickDelta
            ));
        });
    }
}
