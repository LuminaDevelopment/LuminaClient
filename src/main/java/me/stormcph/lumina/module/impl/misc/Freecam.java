package me.stormcph.lumina.module.impl.misc;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.event.impl.Render2DEvent;
import me.stormcph.lumina.mixinterface.CameraInterface;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module {
    private final float minVelocity = 0.001f;
    private boolean wasSneaking = false;
    private Vec3d velocity = Vec3d.ZERO;
    private NumberSetting speed = new NumberSetting("Speed", 0, 2, 0.1, 0.02);
    private BooleanSetting velocitySetting = new BooleanSetting("Velocity", true);
    private BooleanSetting cancelPackets = new BooleanSetting("Cancel Packets", true);
    public Freecam() {
        super("Freecam", "Leave your body and enter spectator mode", Category.MISC);
        addSettings(velocitySetting, speed, cancelPackets);
        noSave();
    }

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        super.onEnable();
        wasSneaking = mc.player.input.sneaking;
    }

    @Override
    public void onDisable() {
        if(nullCheck()) return;
        super.onDisable();
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent e) {
        if (this.isEnabled() && cancelPackets.isEnabled()) e.cancel();
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if(nullCheck()) return;
        if (this.isEnabled()) {
            CameraInterface camera = (CameraInterface) mc.gameRenderer.getCamera();
            if (velocitySetting.isEnabled()) {
                if (mc.options.rightKey.isPressed()) {
                    Vec3d newVec = camera.getRotationVector().rotateY((float) Math.PI / 2).multiply(-speed.getValue());
                    velocity = velocity.add(newVec.x, 0, newVec.z);
                }
                if (mc.options.leftKey.isPressed()) {
                    Vec3d newVec = camera.getRotationVector().rotateY((float) Math.PI / 2).multiply(speed.getValue());
                    velocity = velocity.add(newVec.x, 0, newVec.z);
                }
                if (mc.options.jumpKey.isPressed()) velocity = velocity.add(0, speed.getValue(), 0);
                if (mc.options.sneakKey.isPressed()) velocity = velocity.add(0, -speed.getValue(), 0);
                if (mc.options.forwardKey.isPressed()) {
                    Vec3d newVec = camera.getRotationVector().multiply(speed.getValue());
                    velocity = velocity.add(newVec.x, 0, newVec.z);
                }
                if (mc.options.backKey.isPressed()) {
                    Vec3d newVec = camera.getRotationVector().multiply(-speed.getValue());
                    velocity = velocity.add(newVec.x, 0, newVec.z);
                }

                velocity = new Vec3d(MathHelper.clamp(velocity.x, -speed.getValue() * 3, speed.getValue() * 3), MathHelper.clamp(velocity.y, -speed.getValue() * 3, speed.getValue() * 3), MathHelper.clamp(velocity.z, -speed.getValue() * 3, speed.getValue() * 3));
                velocity = velocity.multiply(0.9);
                if (velocity.x < minVelocity) velocity.multiply(0, 1, 1);
                if (velocity.y < minVelocity) velocity.multiply(1, 0, 1);
                if (velocity.z < minVelocity) velocity.multiply(1, 1, 0);
                camera.moveFreecamBy(velocity);
            } else {
                if (mc.options.rightKey.isPressed()) {
                    Vec3d newVec = camera.getRotationVector().rotateY((float) Math.PI / 2).multiply(-speed.getValue() * 2);
                    camera.moveFreecamBy(new Vec3d(newVec.x, 0, newVec.z));
                }
                if (mc.options.leftKey.isPressed()) {
                    Vec3d newVec = camera.getRotationVector().rotateY((float) Math.PI / 2).multiply(speed.getValue() * 2);
                    camera.moveFreecamBy(new Vec3d(newVec.x, 0, newVec.z));
                }
                if (mc.options.jumpKey.isPressed()) camera.moveFreecamBy(new Vec3d(0, speed.getValue() * 2, 0));
                if (mc.options.sneakKey.isPressed()) camera.moveFreecamBy(new Vec3d(0, -speed.getValue() * 2, 0));
                if (mc.options.forwardKey.isPressed()) {
                    Vec3d newVec = camera.getRotationVector().multiply(speed.getValue() * 2);
                    camera.moveFreecamBy(new Vec3d(newVec.x, 0, newVec.z));
                }
                if (mc.options.backKey.isPressed()) {
                    Vec3d newVec = camera.getRotationVector().multiply(-speed.getValue() * 2);
                    camera.moveFreecamBy(new Vec3d(newVec.x, 0, newVec.z));
                }
            }
        }
    }

    @EventTarget
    public void onEventUpdate(EventUpdate e) {
        if(nullCheck()) return;
        if (this.isEnabled()) {
            if (mc.player.input instanceof KeyboardInput) {
                mc.player.input.sneaking = wasSneaking;
            }
        }
    }
}
