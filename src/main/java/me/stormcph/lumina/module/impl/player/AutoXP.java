package me.stormcph.lumina.module.impl.player;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.RandomizationUtil;
import me.stormcph.lumina.utils.time.TimerUtil;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoXP extends Module {

    private final TimerUtil timerUtil = new TimerUtil();
    NumberSetting delay = new NumberSetting("Delay", 0, 100, 20, 1);
    NumberSetting randomization = new NumberSetting("Randomization", 0, 200, 50, 1);
    BooleanSetting debug = new BooleanSetting("Debug", false);

    public AutoXP() {
        super("AutoXP", "Automatically splashes XP when you hold an experience bottle", Category.PLAYER);
        addSettings(delay, randomization, debug);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onEventUpdate(EventUpdate e) {
        if (mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE) {
            mc.player.swingHand(Hand.MAIN_HAND);
            // calculate random inside the method where it is used0
            int random = RandomizationUtil.randomize(0, (int) randomization.getValue());
            if (timerUtil.hasReached((int) (delay.getValue() + randomization.getValue() + random))) {
                if (debug.isEnabled()) {
                    sendMsg("delay: " + random + randomization.getValue());
                }
                mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                timerUtil.reset();
            }
        }
    }
}
