package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class PearlMacro extends Module {

    private final BooleanSetting throwPearl = new BooleanSetting("Throw", true);
    private final BooleanSetting back = new BooleanSetting("SwitchBack", true);
    private final BooleanSetting debug = new BooleanSetting("Debug", false);

    public PearlMacro() {
        super("PearlMacro", "Switches to a enderpearl, throws it, and then switches back based on your setting choice", Category.GHOST);
        addSettings(throwPearl, back, debug);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if(nullCheck()) {
            toggle();
            return;
        }
        pearlMacro();
    }

    public void pearlMacro() {
        PlayerEntity player = mc.player;
        int originalSlot = player.getInventory().selectedSlot;

        for (int i = 0; i < 9; i++) {
            ItemStack stackInSlot = player.getInventory().getStack(i);
            if (stackInSlot.getItem() == Items.ENDER_PEARL) {
                if (debug.isEnabled()) {
                    sendMsg("EnderPearl was found!");
                }
                player.getInventory().selectedSlot = i;
                if (throwPearl.isEnabled()) {
                    ActionResult result = mc.interactionManager.interactItem(mc.player, mc.player.getActiveHand());
                    if (result == ActionResult.SUCCESS) {
                        if (debug.isEnabled()) {
                            sendMsg("EnderPearl was thrown!");
                        }
                    }
                    if (back.isEnabled()) {
                        player.getInventory().selectedSlot = originalSlot;
                    }
                    setEnabled(false);
                    return;
                }
            }
            else {
                if (debug.isEnabled()) {
                    sendMsg("EnderPearl is not present in hotbar!");
                }
            }
        }
    }

}
