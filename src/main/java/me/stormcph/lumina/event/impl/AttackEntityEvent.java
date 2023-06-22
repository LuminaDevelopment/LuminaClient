package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.entity.Entity;

public class AttackEntityEvent extends Event {

    private Entity entity;

    public AttackEntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
