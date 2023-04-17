package me.stormcph.lumina.module.impl.movement;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PlayerMoveEvent;
import me.stormcph.lumina.mixinterface.IVec3d;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Field;

public class Speed extends Module {

    private final NumberSetting speed = new NumberSetting("Speed", 1, 10, 1, 0.1);

    public Speed() {
        super("Speed", "bhop 4 now", Category.MOVEMENT);
        addSettings(speed);
    }

    @EventTarget
    public void onMove(PlayerMoveEvent event) {
        if (mc.player == null) return;

        if(mc.player.isOnGround()) mc.player.jump();

        Vec3d vel = getHorizontalVelocity(speed.getFloatValue());

        double velX = vel.getX();
        double velZ = vel.getZ();

        event.set(velX, event.getPos().y, velZ);
    }

    private final Vec3d horizontalVelocity = new Vec3d(0, 0, 0);

    public Vec3d getHorizontalVelocity(double bps) {
        float yaw = mc.player.getYaw();

        double diagonal = 1 / Math.sqrt(2);

        Vec3d forward = Vec3d.fromPolar(0, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);
        double velX = 0;
        double velZ = 0;

        boolean a = false;
        if (mc.player.input.pressingForward) {
            velX += forward.x / 20 * bps;
            velZ += forward.z / 20 * bps;
            a = true;
        }
        if (mc.player.input.pressingBack) {
            velX -= forward.x / 20 * bps;
            velZ -= forward.z / 20 * bps;
            a = true;
        }

        boolean b = false;
        if (mc.player.input.pressingRight) {
            velX += right.x / 20 * bps;
            velZ += right.z / 20 * bps;
            b = true;
        }
        if (mc.player.input.pressingLeft) {
            velX -= right.x / 20 * bps;
            velZ -= right.z / 20 * bps;
            b = true;
        }

        if (a && b) {
            velX *= diagonal;
            velZ *= diagonal;
        }

        ((IVec3d) horizontalVelocity).setX(velX);
        ((IVec3d) horizontalVelocity).setZ(velZ);

        return horizontalVelocity;
    }
}
