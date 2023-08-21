package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.PlayerMoveEvent;
import me.stormcph.lumina.mixinterface.CameraInterface;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.misc.Freecam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
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
    MinecraftClient mc = MinecraftClient.getInstance();

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void onMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        if (this.isPlayer() && ModuleManager.INSTANCE.getModuleByClass(Freecam.class).isEnabled()) ci.cancel();
        if(!(((Object) this) instanceof ClientPlayerEntity)) return;
        if(!(((Object) this) == MinecraftClient.getInstance().player)) return;

        PlayerMoveEvent event = new PlayerMoveEvent(movementType, movement);
        event.call();
        if (event.cancelled) {
            ci.cancel();
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void onPushAwayFrom(Entity entity, CallbackInfo ci) {
        if (this.isPlayer() && ModuleManager.INSTANCE.getModuleByClass(Freecam.class).isEnabled()) ci.cancel();
    }

    @Inject(method = "setYaw", at = @At("HEAD"), cancellable = true)
    public void setYaw(float yaw, CallbackInfo ci) {
        if (this.isPlayer() && ModuleManager.INSTANCE.getModuleByClass(Freecam.class).isEnabled()) {
            if (mc.currentScreen != null) {
                ci.cancel();
                return;
            }
            Camera camera = mc.gameRenderer.getCamera();
            ((CameraInterface) camera).setFreecamYaw(camera.getYaw() + yaw - this.yaw);
            if (((Freecam) ModuleManager.INSTANCE.getModuleByClass(Freecam.class)).getRotationMode() == 2) {
                Vec3d viewPos = mc.player.getEyePos();
                Vec3d targetPos = mc.crosshairTarget.getPos();
                Vec3d difPos = new Vec3d(targetPos.x - viewPos.x, targetPos.y - viewPos.y, targetPos.z - viewPos.z);
                mc.player.headYaw = (float) -(Math.atan2(difPos.x, difPos.z) * 180/Math.PI);
                mc.player.bodyYaw = 0;//mc.player.headYaw;
            }
            if (((Freecam) ModuleManager.INSTANCE.getModuleByClass(Freecam.class)).getRotationMode() != 1) ci.cancel();
        }
    }

    @Inject(method = "setPitch", at = @At("HEAD"), cancellable = true)
    public void setPitch(float pitch, CallbackInfo ci) {
        if (this.isPlayer() && ModuleManager.INSTANCE.getModuleByClass(Freecam.class).isEnabled()) {
            if (mc.currentScreen != null) {
                ci.cancel();
                return;
            }
            Camera camera = mc.gameRenderer.getCamera();
            ((CameraInterface) camera).setFreecamPitch(camera.getPitch() + pitch - this.pitch);
            if (((Freecam) ModuleManager.INSTANCE.getModuleByClass(Freecam.class)).getRotationMode() != 1) ci.cancel();
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
