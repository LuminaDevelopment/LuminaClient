package me.stormcph.lumina.mixins;

import me.stormcph.lumina.Lumina;
import me.stormcph.lumina.config.ConfigWriter;
import me.stormcph.lumina.event.impl.EventUpdate;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        new EventUpdate().call();
    }

    @Inject(method = "scheduleStop", at = @At("HEAD"))
    public void stop(CallbackInfo ci){
        ConfigWriter.writeConfig();
    }

}
