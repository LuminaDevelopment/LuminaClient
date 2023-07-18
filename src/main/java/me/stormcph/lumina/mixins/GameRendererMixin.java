package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.NoHurtCam;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at={@At(value="HEAD")}, method={"bobViewWhenHurt"}, cancellable=true)
    public void bobViewWhenHurt(MatrixStack matrixStack_1, float float_1, CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByClass(NoHurtCam.class).isEnabled()) {
            ci.cancel();
        }
    }
}
