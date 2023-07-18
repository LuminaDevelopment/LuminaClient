package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.util.Hand;

public class HandSwingEvent extends Event {

    private final Hand hand;

    public HandSwingEvent(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }
}
