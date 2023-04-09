package me.stormcph.lumina.mixins;

import me.stormcph.lumina.ui.Hud;
import net.fabricmc.fabric.mixin.client.rendering.InGameHudMixin;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class inGameHudMixin {

    @Inject(method = "render", at = @At("RETURN"), cancellable = true)
    public void renderHud(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Hud.render(matrices, tickDelta);
    }
}
