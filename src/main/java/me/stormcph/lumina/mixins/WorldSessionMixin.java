package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.Event;
import me.stormcph.lumina.event.impl.OnWorldLoadEvent;
import net.minecraft.client.util.telemetry.WorldSession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldSession.class)
public class WorldSessionMixin {

    @Inject(method = "onLoad", at = @At("HEAD"), cancellable = true)
    private static void onWorldLoad(CallbackInfo ci){
        Event e = new OnWorldLoadEvent().call();
        if(e.isCancelled()) ci.cancel();
    }


}
