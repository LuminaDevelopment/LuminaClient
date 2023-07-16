package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class PlayerInteractItemEvent extends Event {

    private final PlayerEntity player;
    private final Hand hand;

    public PlayerInteractItemEvent(PlayerEntity player, Hand hand) {
        this.player = player;
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    public PlayerEntity getPlayer() {
        return player;
    }
}
