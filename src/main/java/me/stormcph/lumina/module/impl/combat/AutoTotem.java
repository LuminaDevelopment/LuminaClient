package me.stormcph.lumina.module.impl.combat;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {

    //private final BooleanSetting excludeHotbar = new BooleanSetting("ExcludeHotbar", true); i tried but i couldent get it to work, note that the excludeHotbar is already there but i cant get it to deploy the totem in the offhand from hotbar

    public AutoTotem() {
        super("AutoTotem", "Automatically deploys a totem in the players offhand (blatent)", Category.COMBAT);
    }

    @EventTarget
    public void onEventUpdate(EventUpdate e) {
        if (mc.player == null) return;
        if (mc.player.getOffHandStack().getItem().equals(Items.TOTEM_OF_UNDYING)) return;

        int i;
        boolean found = false;
        for (i = 9; i <= 36; i++) {
            if (mc.player.getInventory().getStack(i).getItem().equals(Items.TOTEM_OF_UNDYING)) {
                found = true;
                break;
            }
        }

        if (found) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, mc.player);
        }
    }
}
