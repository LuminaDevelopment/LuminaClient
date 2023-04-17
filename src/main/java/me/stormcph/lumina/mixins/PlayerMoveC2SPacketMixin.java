package me.stormcph.lumina.mixins;

import me.stormcph.lumina.mixinterface.IPMC2SP;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerMoveC2SPacket.class)
public class PlayerMoveC2SPacketMixin implements IPMC2SP {

    @Final @Shadow @Mutable protected double x;
    @Final @Shadow @Mutable protected double y;
    @Final @Shadow @Mutable protected double z;
    @Final @Shadow @Mutable protected float yaw;
    @Final @Shadow @Mutable protected float pitch;

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
