package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.PacketReceiveEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Inject(method = "channelRead0", at = @At("HEAD"))
    private void onPacketReceive(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        PacketReceiveEvent event = new PacketReceiveEvent(packet);
        event.call();
        if(event.isCancelled()) ci.cancel();
    }
}
