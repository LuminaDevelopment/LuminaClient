package me.stormcph.lumina.module.impl.movement;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;

public class Velocity extends Module {

    NumberSetting horizontal = new NumberSetting("Horizontal", 0, 100, 100, 1);
    NumberSetting vertical = new NumberSetting("Vertical", 0, 100, 100, 1);
    public Velocity() {
        super("noKB", "Reduces knockback", Category.MOVEMENT);
        addSettings(horizontal, vertical);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onEventUpdate(EventUpdate e) {
        if (nullCheck()) return;
        mc.player.velocityModified = true;
        if (mc.player.hurtTime > 1) {
            mc.player.setVelocity(mc.player.getVelocity().x / 100 / horizontal.getFloatValue(), mc.player.getVelocity().y, mc.player.getVelocity().z / 100 / horizontal.getFloatValue());
        }
        if (mc.player.hurtTime > 1) {
            mc.player.setVelocity(mc.player.getVelocity().x, mc.player.getVelocity().y / 100 / vertical.getFloatValue(), mc.player.getVelocity().z);
        }
    }
}
