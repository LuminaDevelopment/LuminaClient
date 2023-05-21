package me.stormcph.lumina.mixins;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LogUtils.class)
public class LogUtilsMixin{
    @Inject(method = "getLogger", at = @At("HEAD"))
    private static void getLogger(CallbackInfoReturnable<Logger> cir) {
        // removed
    }
}
