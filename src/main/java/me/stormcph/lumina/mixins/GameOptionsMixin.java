package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.misc.Freecam;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Inject(method = "setPerspective", at = @At("HEAD"), cancellable = true)
    private void onSetPerspective(CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByClass(Freecam.class).isEnabled()) ci.cancel();
    }
}