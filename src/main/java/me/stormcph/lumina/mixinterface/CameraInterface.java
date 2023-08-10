package me.stormcph.lumina.mixinterface;

import net.minecraft.util.math.Vec3d;

public interface CameraInterface {
    default void moveFreecamBy(Vec3d pos) {};
    default void setFreecamPos(Vec3d pos) {};
    default void setFreecamYaw(float yaw) {};
    default void setFreecamPitch(float pitch) {};
    default void setFreecamRotation(float yaw, float pitch) {};
    default Vec3d getRotationVector() { return Vec3d.ZERO; };
    float getPitch();
    float getYaw();
    double getCameraX();
    double getCameraY();
    double getCameraZ();
}
