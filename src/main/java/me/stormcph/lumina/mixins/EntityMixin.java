package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.PlayerMoveEvent;
import me.stormcph.lumina.mixinterface.CameraInterface;
import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract boolean isPlayer();
    @Shadow private float yaw;
    @Shadow private float pitch;
    @Shadow protected abstract Vec3d getRotationVector(float pitch, float yaw);

    private MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void onMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        if (this.isPlayer() && ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) ci.cancel();
        if (!(((Object) this) instanceof ClientPlayerEntity)) return;
        if (!(((Object) this) == MinecraftClient.getInstance().player)) return;

        PlayerMoveEvent event = new PlayerMoveEvent(movementType, movement);
        event.call();
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void onPushAwayFrom(Entity entity, CallbackInfo ci) {
        if (this.isPlayer() && ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) ci.cancel();
    }

    @Inject(method = "setYaw", at = @At("HEAD"), cancellable = true)
    public void setYaw(float yaw, CallbackInfo ci) {
        if (this.isPlayer() && ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) {
            Camera camera = mc.gameRenderer.getCamera();
            ((CameraInterface) camera).setFreecamYaw(camera.getYaw() + yaw - this.yaw);
            ci.cancel();
        }
    }

    @Inject(method = "setPitch", at = @At("HEAD"), cancellable = true)
    public void setPitch(float pitch, CallbackInfo ci) {
        if (this.isPlayer() && ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled()) {
            Camera camera = mc.gameRenderer.getCamera();
            ((CameraInterface) camera).setFreecamPitch(camera.getPitch() + pitch - this.pitch);
            ci.cancel();
        }
    }

    @Inject(method = "getCameraPosVec", at = @At("RETURN"), cancellable = true)
    public void getCameraPosVec(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        cir.setReturnValue(mc.gameRenderer.getCamera().getPos());
    }

    @Inject(method = "getRotationVec", at = @At("RETURN"), cancellable = true)
    public void getRotationVec(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        cir.setReturnValue(this.getRotationVector(mc.gameRenderer.getCamera().getPitch(), mc.gameRenderer.getCamera().getYaw()));
    }
}