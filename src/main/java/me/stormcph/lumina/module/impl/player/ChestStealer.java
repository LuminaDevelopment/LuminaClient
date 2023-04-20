package me.stormcph.lumina.module.impl.player;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public class ChestStealer extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", 0.0, 1000.0, 150.0, 0.1);
    private TimerUtil timer;

    public ChestStealer() {
        super("ChestStealer", "Steals stuff outa chests", Category.PLAYER);
        addSettings(delay);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        timer = new TimerUtil();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

        if(mc.currentScreen instanceof GenericContainerScreen gcs) {
            if(gcs.getTitle().contains(Text.of("Chest"))) {

                if(gcs.getScreenHandler().getInventory().isEmpty()) {
                    mc.player.closeScreen();
                    return;
                }

                for(int i = 0; i < gcs.getScreenHandler().getInventory().size();) {
                    ItemStack item = gcs.getScreenHandler().getSlot(i).getStack();
                    if(item != null && !(item.getItem() instanceof AirBlockItem) && timer.hasReached(delay.getValue())) {
                        sendMsg(i + ": " + gcs.getScreenHandler().getSlot(i).getStack().getItem().getName().getString());
                        mc.interactionManager.clickSlot(gcs.getScreenHandler().syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
                        timer.reset();
                    }
                    i++;
                }
            }
        }
    }
}
