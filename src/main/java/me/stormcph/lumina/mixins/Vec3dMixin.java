package me.stormcph.lumina.mixins;

import me.stormcph.lumina.mixinterface.IVec3d;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Vec3d.class)
public class Vec3dMixin implements IVec3d {

    @Mutable @Final @Shadow public double x;
    @Mutable @Final @Shadow public double y;
    @Mutable @Final @Shadow public double z;

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
}
