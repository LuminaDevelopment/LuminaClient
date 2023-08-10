package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.misc.Freecam;
import me.stormcph.lumina.module.impl.render.NoHurtCam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    public abstract void render(float tickDelta, long startTime, boolean tick);

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void tiltViewWhenHurt(CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByClass(Freecam.class).isEnabled()) ci.cancel();
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void bobView(CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByClass(Freecam.class).isEnabled()) ci.cancel();
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if (ModuleManager.INSTANCE.getModuleByClass(Freecam.class).isEnabled())
            cir.setReturnValue((double) MinecraftClient.getInstance().options.getFov().getValue().intValue());
    }

    // net.minecraft.client.render.GameRenderer#tiltViewWhenHurt:2
    @Inject(at = {@At(value = "HEAD")}, method = {"tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"}, cancellable = true)
    public void bobViewWhenHurt(MatrixStack matrixStack_1, float float_1, CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByClass(NoHurtCam.class).isEnabled()) {
            ci.cancel();
        }
    }
}
