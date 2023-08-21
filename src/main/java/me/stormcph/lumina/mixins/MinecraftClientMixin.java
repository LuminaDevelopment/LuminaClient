package me.stormcph.lumina.mixins;

import me.stormcph.lumina.Lumina;
import me.stormcph.lumina.config.ConfigReader;
import me.stormcph.lumina.config.ConfigWriter;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.ESP;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow public abstract boolean isPaused();

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        new EventUpdate().call();
    }

    @Inject(method = "scheduleStop", at = @At("HEAD"))
    public void stop(CallbackInfo ci){
        Thread configThr = new Thread(() -> ConfigWriter.writeConfig(false, null), "LuminaConfigWriterThread");
        configThr.start();
    }

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    public void hasOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(ModuleManager.INSTANCE.getModuleByClass(ESP.class).isEnabled() && ESP.mode.getMode().equalsIgnoreCase("Glow")) {
            if((entity instanceof ClientPlayerEntity || entity instanceof OtherClientPlayerEntity) && entity != MinecraftClient.getInstance().player) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("HEAD"))
    private void disconnect(Screen screen, CallbackInfo ci) {
        for (Module m : ModuleManager.INSTANCE.getModules()) if (m.disablesOnExit()) m.setEnabled(false);
    }
}
