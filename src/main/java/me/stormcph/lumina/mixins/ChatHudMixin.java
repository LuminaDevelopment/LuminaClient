package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.ChatMessageReceivedEvent;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At("HEAD"), cancellable = true)
    public void onAddMessage(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo ci) {
        ChatMessageReceivedEvent chatMessageReceivedEvent = new ChatMessageReceivedEvent(message);
        chatMessageReceivedEvent.call();
        if(chatMessageReceivedEvent.cancelled) {
            ci.cancel();
        }
    }
}
