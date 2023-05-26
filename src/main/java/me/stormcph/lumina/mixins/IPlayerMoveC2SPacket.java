package me.stormcph.lumina.mixins;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerMoveC2SPacket.class)
public interface IPlayerMoveC2SPacket {
    @Accessor("x")
    double lumina$getX();

    @Accessor("x")
    void lumina$setX(double x);

    @Accessor("y")
    double lumina$getY();

    @Accessor("y")
    void lumina$setY(double y);

    @Accessor("z")
    double lumina$getZ();

    @Accessor("z")
    void lumina$setZ(double z);

    @Accessor("yaw")
    float lumina$getYaw();

    @Accessor("yaw")
    void lumina$setYaw(float yaw);

    @Accessor("pitch")
    float lumina$getPitch();

    @Accessor("pitch")
    void lumina$setPitch(float yaw);
}
