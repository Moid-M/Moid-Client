package dev.moid.client.mixin;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientBrandRetriever.class, remap = false)
public class MixinClientBrandCustomPayload {

    @Inject(method = "getClientModName", at = @At("HEAD"), cancellable = true, remap = false)
    private static void spoofBrand(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("vanilla");
    }
}
