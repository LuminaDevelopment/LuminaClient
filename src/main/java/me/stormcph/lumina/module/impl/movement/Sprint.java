package me.stormcph.lumina.module.impl.movement;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.PlayerMoveEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;

public class Sprint extends Module {

    ModeSetting bypassMode = new ModeSetting("Bypass", "None",  "None", "Basic");

    public Sprint() {
        super("Sprint", "Keeps your sprint", Category.MOVEMENT);
        //this.setKey(GLFW.GLFW_KEY_V);
        addSettings(bypassMode);

    }

    @EventTarget
    public void onUpdate(EventUpdate e){
        if(bypassMode.isMode("None")){
            mc.player.setSprinting(true);
        }
    }

    @EventTarget
    public void onMove(PlayerMoveEvent e) {
        if(
                bypassMode.isMode("Basic") &&
                mc.player.forwardSpeed > 0 && !mc.player.isUsingItem() && !mc.player.isSneaking()
        ) {
            mc.player.setSprinting(true);
        }
    }
}
