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
    public BooleanSetting testBool = new BooleanSetting("Check", true);
    public ModeSetting testMode = new ModeSetting("Mode", "Test", "Test 2", "Test 3");

    public Flight() {
        super("Flight", "Vanilla fly, instant ban with Anti-Cheat.", Category.MOVEMENT);
        addSettings(speed, testBool, testMode);
        //this.setKey(GLFW.GLFW_KEY_G);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlySpeed((float) speed.getFloatValue());
    }

    @Override
    public void onDisable() {
        mc.player.getAbilities().flying = false;
        super.onDisable();
    }
}
