package me.stormcph.lumina.module.impl.movement;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;

public class Flight extends Module {

    public NumberSetting speed = new NumberSetting("Speed", 0, 4, 0.3, 0.05);

    public Flight() {
        super("Flight", "Vanilla fly, instant ban with Anti-Cheat.", Category.MOVEMENT);
        addSettings(speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(nullCheck()) return;
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlySpeed(speed.getFloatValue());
    }

    @Override
    public void onDisable() {
        if(nullCheck()) return;
        mc.player.getAbilities().flying = false;
        super.onDisable();
    }
}
