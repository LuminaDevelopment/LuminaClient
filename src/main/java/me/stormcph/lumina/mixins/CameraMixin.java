package me.stormcph.lumina.mixins;

import me.stormcph.lumina.mixinterface.CameraInterface;
import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin implements CameraInterface {
    @Shadow private Vec3d pos;
    @Shadow private float yaw;

    @Shadow private float pitch;

    @Shadow @Final private Quaternionf rotation;

    @Shadow @Final private Vector3f horizontalPlane;

    @Shadow @Final private Vector3f verticalPlane;

    @Shadow @Final private Vector3f diagonalPlane;

    @Shadow protected abstract void setRotation(float yaw, float pitch);

    // prevent moving/rotating with player
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) ci.cancel();
    }

    // show player model
    @Inject(method = "isThirdPerson", at = @At("RETURN"), cancellable = true)
    public void isThirdPerson(CallbackInfoReturnable cir) {
        if (ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) cir.setReturnValue(true);
    }

    @Inject(method = "updateEyeHeight", at = @At("RETURN"), cancellable = true)
    public void isThirdPerson(CallbackInfo ci) {
        if (ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) ci.cancel();
    }

    @Inject(method = "getSubmersionType", at = @At("HEAD"), cancellable = true)
    public void getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) cir.setReturnValue(CameraSubmersionType.NONE);
    }

    public void moveFreecamBy(Vec3d pos) {
        this.pos = new Vec3d(this.pos.x + pos.x, this.pos.y + pos.y, this.pos.z + pos.z);
    }

    public void setFreecamPos(Vec3d pos) {
        this.pos = pos;
    }

    public void setFreecamRotation(float yaw, float pitch) {
        setRotation(yaw, pitch);
    }
    public void setFreecamYaw(float yaw) {
        this.setRotation(yaw, pitch);
    }
    public void setFreecamPitch(float pitch) {
        this.setRotation(yaw, pitch);
    }

    public Vec3d getRotationVector() {
        float f = this.pitch * ((float)Math.PI / 180);
        float g = -this.yaw * ((float)Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }
}