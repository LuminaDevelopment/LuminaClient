package me.stormcph.lumina.mixins;

import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Vec3d.class)
public interface IVec3d {
    @Accessor("x")
    void lumina$setX(double x);

    @Accessor("y")
    void lumina$setY(double y);

    @Accessor("z")
    void lumina$setZ(double z);
}
