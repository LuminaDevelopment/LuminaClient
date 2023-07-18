package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.ChatMessageSentEvent;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "sendPacket*", at = @At("HEAD"), cancellable = true)
    public void onSend(Packet<?> packet, CallbackInfo ci) {
        PacketSendEvent pse = new PacketSendEvent(packet);
        pse.call();
        if(pse.cancelled) ci.cancel();
    }
}
