package me.stormcph.lumina.mixins;

import me.stormcph.lumina.Lumina;
import me.stormcph.lumina.event.impl.KeyEvent;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.security.PublicKey;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        KeyEvent keyEvent = new KeyEvent(key, action);
        keyEvent.call();

        if(keyEvent.isCancelled()) ci.cancel();
    }
}
