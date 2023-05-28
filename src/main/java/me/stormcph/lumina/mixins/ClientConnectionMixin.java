package me.stormcph.lumina.mixins;

import me.stormcph.lumina.mixinterface.IPMC2SP;
import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Shadow
    protected abstract void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks);
    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
    public void send(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
       if (packet instanceof PlayerMoveC2SPacket castPacket) {
           if (ModuleManager.INSTANCE.getModuleByName("NoFall").isEnabled()) {
               ci.cancel();
               IPMC2SP newPacket = ((IPMC2SP)castPacket);
               newPacket.setOnGround(true);
               sendImmediately((Packet)newPacket, null);
           }
       }
    }
}
