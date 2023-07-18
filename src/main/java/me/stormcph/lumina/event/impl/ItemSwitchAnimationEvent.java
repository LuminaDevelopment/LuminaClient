package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;

public class ItemSwitchAnimationEvent extends Event {

    private float equipProgressMainHand;

    public ItemSwitchAnimationEvent(float equipProgressMainHand) {
        this.equipProgressMainHand = equipProgressMainHand;
    }

    public float getEquipProgressMainHand() {
        return equipProgressMainHand;
    }

    public void setEquipProgressMainHand(float equipProgressMainHand) {
        this.equipProgressMainHand = equipProgressMainHand;
    }
}
