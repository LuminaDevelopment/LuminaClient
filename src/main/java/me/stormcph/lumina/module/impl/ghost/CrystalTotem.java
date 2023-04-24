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
    private final NumberSetting searchRadiusX = new NumberSetting("Radius X", 0.0, 16.0, 8.0, 0.01);
    private final NumberSetting searchRadiusYPlus = new NumberSetting("Radius (above) Y", 0.0, 16.0, 5.0, 0.01);
    private final NumberSetting searchRadiusYminus = new NumberSetting("Radius (below) Y", 0.0, 1.0, 0.6, 0.01);
    private final NumberSetting searchRadiusZ = new NumberSetting("Radius Z", 0.0, 16.0, 8.0, 0.01);



    public CrystalTotem() {
        super("AutoDoubleHand", "Automatically pops end crystal when placed", Category.GHOST);
        addSettings(cooldown, searchRadiusX, searchRadiusYPlus, searchRadiusYminus, searchRadiusZ);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                ClientPlayerEntity player = client.player;
                if (player != null) {
                    checkEndCrystal(player);
                }
            }
        });
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    }

    private void checkEndCrystal(ClientPlayerEntity player) {
        double radiusX = searchRadiusX.getValue();
        double radiusY = searchRadiusYPlus.getValue();
        double radiusZ = searchRadiusZ.getValue();
        double radiusBelowY = searchRadiusYminus.getValue();

        double minY = player.getY() - radiusY;
        double maxY = player.getY() + radiusY;

        double tolerance = 0.01;

        for (Entity entity : player.world.getEntitiesByClass(EndCrystalEntity.class, player.getBoundingBox().expand(radiusX, radiusY, radiusZ), endCrystal -> endCrystal.getY() >= minY && endCrystal.getY() <= maxY)) {
            double heightDifferenceAbove = player.getY() - entity.getY();
            double heightDifferenceBelow = entity.getY() - player.getY();

            if ((heightDifferenceAbove >= 0 && heightDifferenceAbove <= radiusY + tolerance) || (heightDifferenceBelow >= 0 && heightDifferenceBelow <= radiusBelowY + tolerance)) {
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
