package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.time.TimerUtil;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;

public class AutoRefill extends Module {

    private final ModeSetting blatant = new ModeSetting("mode", "ghost", "ghost", "blatant");
    private final NumberSetting cooldown = new NumberSetting("cooldown-ms", 0, 1000, 300, 0.1);
    private final BooleanSetting[] slotSettings = new BooleanSetting[] {
            new BooleanSetting("slot1", true),
            new BooleanSetting("slot2", true),
            new BooleanSetting("slot3", true),
            new BooleanSetting("slot4", true),
            new BooleanSetting("slot5", true),
            new BooleanSetting("slot6", true),
            new BooleanSetting("slot7", true),
            new BooleanSetting("slot8", true),
            new BooleanSetting("slot9", true),
    };
    private TimerUtil timer = new TimerUtil();

    public AutoRefill() {
        super("AutoRefill", "refills certain items in hotbar from inventory", Category.GHOST);
        super.addSettings(blatant, cooldown);
        for (BooleanSetting setting : slotSettings) {
            super.addSettings(setting);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        //refillHotbar();
    }


    @EventTarget
    public void onEventUpdate(EventUpdate e) {
        if (!timer.hasReached(cooldown.getValue())) {
            return;
        }
        timer.reset();

        if ("ghost".equals(blatant.getMode()) && !(mc.currentScreen instanceof InventoryScreen)) {
            return;
        }

        for (int i = 0; i < 9; i++) {
            if (!slotSettings[i].isEnabled()) {
                continue;
            }
            ItemStack hotbarStack = mc.player.getInventory().getStack(i);
            if (!hotbarStack.isEmpty() && hotbarStack.getCount() < hotbarStack.getMaxCount()) {
                for (int j = 9; j < mc.player.getInventory().size(); j++) {
                    ItemStack inventoryStack = mc.player.getInventory().getStack(j);
                    if (!inventoryStack.isEmpty() && inventoryStack.getItem() == hotbarStack.getItem()) {
                        int transferAmount = Math.min(hotbarStack.getMaxCount() - hotbarStack.getCount(), inventoryStack.getCount());
                        hotbarStack.increment(transferAmount);
                        inventoryStack.decrement(transferAmount);
                        if (inventoryStack.isEmpty()) {
                            mc.player.getInventory().removeStack(j);
                        }
                        if (hotbarStack.getCount() == hotbarStack.getMaxCount()) {
                            break;
                        }
                    }
                }
            }
        }
    }
}

