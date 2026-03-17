package dev.moid.client.module.modules;

import dev.moid.client.module.Category;
import dev.moid.client.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FullBright extends Module {

    public FullBright() {
        super("FullBright", "Gives permanent night vision without the effect indicator", Category.MISC);
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        client.player.addStatusEffect(
            new StatusEffectInstance(
                StatusEffects.NIGHT_VISION,
                Integer.MAX_VALUE, // duration essentially infinite
                0,                 // amplifier
                false,             // ambient - hides particles
                false,             // show particles
                false              // show icon - hides the HUD icon
            )
        );
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        client.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}
