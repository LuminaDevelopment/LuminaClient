package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;


public class CrystalTotem extends Module {

    public CrystalTotem() {
        super("CrystalTotem", "Automatically pops end crystal when placed", Category.GHOST);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                ClientPlayerEntity player = client.player;
                if (player != null) {
                    checkEndCrystalYLevel(player);
                }
            }
        });
    }

    private void checkEndCrystalYLevel(ClientPlayerEntity player) {
        double searchRadius = 16.0; // You can adjust the search radius as needed
        double minY = player.getY() - searchRadius;
        double maxY = player.getY() + searchRadius;

        for (Entity entity : player.world.getEntitiesByClass(EndCrystalEntity.class, player.getBoundingBox().expand(searchRadius, searchRadius, searchRadius), endCrystal -> endCrystal.getY() >= minY && endCrystal.getY() <= maxY)) {
            if (Math.abs(player.getY() - entity.getY()) < 0.5) { // You can adjust the tolerance as needed
                sendMsg("detected!!!!!!!!!!!!!!!!!");
            } else {
                // Do nothing
            }
        }
    }
}