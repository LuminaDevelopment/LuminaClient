package me.stormcph.lumina.module.impl.combat;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.AttackEntityEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.time.TimerUtil;
import org.lwjgl.glfw.GLFW;

public class AutoDTap extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", 0, 1000, 10, 1);
    private TimerUtil timer;

    public AutoDTap() {
        super("WTap", "Very simple, will just stop you from moving when attacking an entity", Category.COMBAT);
        addSettings(delay);
    }

    @Override
    public void onEnable() {
        timer = new TimerUtil();
        super.onEnable();
    }

    @EventTarget
    public void onAttack(AttackEntityEvent e) {
        mc.options.forwardKey.setPressed(false);

    }
}
