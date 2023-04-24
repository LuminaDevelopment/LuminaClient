package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class CrystalTotem extends Module {

    private final TimerUtil timerUtil = new TimerUtil();

    private final NumberSetting cooldown = new NumberSetting("SwitchDelay", 0.0, 1000.0, 50.0, 0.01);
    NumberSetting range = new NumberSetting("Search radius", 0.0, 16.0, 4.0, 0.1);

    public CrystalTotem() {
        super("AutoDoubleHand", "Automatically pops end crystal when placed", Category.GHOST);
        addSettings(range, cooldown);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                ClientPlayerEntity player = client.player;
                if (player != null) {
                    checkEndCrystalYLevel(player);
                }
            }
        });
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    }

    private void checkEndCrystalYLevel(ClientPlayerEntity player) {
        double searchRadius = range.getValue();
        double minY = player.getY() - searchRadius;
        double maxY = player.getY() + searchRadius;

        for (Entity entity : player.world.getEntitiesByClass(EndCrystalEntity.class, player.getBoundingBox().expand(searchRadius, searchRadius, searchRadius), endCrystal -> endCrystal.getY() >= minY && endCrystal.getY() <= maxY)) {
            if (Math.abs(player.getY() - entity.getY()) < 0.5) {
                if (mc.player == null || mc.world == null || mc.isPaused()) {
                    return;
                }

                for (int i = 0; i < 9; i++) {
                    Item item = mc.player.getInventory().getStack(i).getItem();
                    if (item == Items.TOTEM_OF_UNDYING) {
                        if (timerUtil.hasReached((int) cooldown.getValue())) {
                            mc.player.getInventory().selectedSlot = i;
                            timerUtil.reset();
                        }
                            break;
                    }
                }
            }
        }
    }
}
