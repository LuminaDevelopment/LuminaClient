package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.time.TimerUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class PearlMacro extends Module {

    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil switchBackTimer = new TimerUtil();
    private final TimerUtil throwPearlTimer = new TimerUtil();
    private final NumberSetting switchBackDelay = new NumberSetting("SwitchBackDelay", 0, 1000, 39, 1);
    private final NumberSetting throwPearlDelay = new NumberSetting("ThrowPearlDelay", 0, 1000, 94, 1);
    private final BooleanSetting throwPearl = new BooleanSetting("Throw", true);
    private final BooleanSetting back = new BooleanSetting("SwitchBack", true);
    private final BooleanSetting debug = new BooleanSetting("Debug", false);

    public PearlMacro() {
        super("PearlMacro", "Switches to a enderpearl, throws it, and then switches back based on your setting choice", Category.GHOST);
        addSettings(throwPearl, back, debug, switchBackDelay, throwPearlDelay);
    }


    @Override
    public void onEnable() {
        super.onEnable();
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            pearlMacro(player);
        }
    }

    @EventTarget
    public void onUpdate() {

    }

    public boolean pearlMacro(PlayerEntity player) {
        int originalSlot = player.getInventory().selectedSlot;

        for (int i = 0; i < 9; i++) {
            ItemStack stackInSlot = player.getInventory().getStack(i);
            if (stackInSlot.getItem() == Items.ENDER_PEARL) {
                if (debug.isEnabled()) {
                    sendMsg("EnderPearl was found!");
                }
                player.getInventory().selectedSlot = i;
                if (throwPearl.isEnabled()) {
                    if (throwPearlTimer.hasReached(throwPearlDelay.getValue())) {
                        ActionResult result = mc.interactionManager.interactItem(mc.player, mc.player.getActiveHand());
                        if (result == ActionResult.SUCCESS) {
                            if (debug.isEnabled()) {
                                sendMsg("EnderPearl was thrown!");
                            }
                        }
                        throwPearlTimer.reset();
                    }
                    if (back.isEnabled()) {
                        if (switchBackTimer.hasReached(switchBackDelay.getValue())) {
                            player.getInventory().selectedSlot = originalSlot;
                            if (debug.isEnabled()) {
                                sendMsg("Switched back to original slot!");
                            }
                            switchBackTimer.reset();
                        }
                    }
                    else {
                        if (debug.isEnabled()) {
                            sendMsg("SwitchBack is disabled!");
                        }
                    }
                    setEnabled(false);
                    return true;
                }
            }
            else {
                if (debug.isEnabled()) {
                    sendMsg("EnderPearl is not present in hotbar!");
                }
            }
        }
        return false;
    }
}