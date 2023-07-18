package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.ChatMessageSentEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerMixin {

    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    public void sendChatMessage(String msg, Text preview, CallbackInfo ci) {
        ChatMessageSentEvent cmse = new ChatMessageSentEvent(msg);
        cmse.call();
        if(cmse.cancelled) ci.cancel();
    }
}
