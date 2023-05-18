package me.stormcph.lumina.event.impl;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;

public interface PlayerThrowEnderpearlEvent {
    Event<PlayerThrowEnderpearlEvent> EVENT = EventFactory.createArrayBacked(PlayerThrowEnderpearlEvent.class,
            (listeners) -> (player, enderPearl) -> {
                for (PlayerThrowEnderpearlEvent listener : listeners) {
                    listener.interact(player, enderPearl);
                }
            }
    );

    void interact(PlayerEntity player, EnderPearlEntity enderPearl);
}