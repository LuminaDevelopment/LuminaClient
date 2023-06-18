package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void tiltViewWhenHurt(CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) ci.cancel();
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void bobView(CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) ci.cancel();
    }

    @Inject(method = "getFov", at =  @At("RETURN"), cancellable = true)
    private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if (ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) cir.setReturnValue((double) MinecraftClient.getInstance().options.getFov().getValue().intValue());
    }
}