package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Field;

public class PlayerMoveEvent extends Event {

    private final MovementType movementType;
    private Vec3d pos;

    public PlayerMoveEvent(MovementType movementType, Vec3d pos) {
        this.movementType = movementType;
        this.pos = pos;
    }

    public Vec3d getPos() {
        return pos;
    }

    public void setPos(Vec3d pos) {
        this.pos = pos;
    }

    public void set(double x, double y, double z) {
        try {
            Field xField = pos.getClass().getField("x");
            xField.setAccessible(true);
            xField.set(pos, x);

            Field yField = pos.getClass().getField("y");
            yField.setAccessible(true);
            yField.set(pos, y);

            Field zField = pos.getClass().getField("z");
            zField.setAccessible(true);
            zField.set(pos, z);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public MovementType getMovementType() {
        return movementType;
    }
}
