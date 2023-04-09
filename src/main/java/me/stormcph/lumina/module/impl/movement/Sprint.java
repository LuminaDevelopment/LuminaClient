package me.stormcph.lumina.module.impl.movement;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Keeps your sprint", Category.MOVEMENT);
        //this.setKey(GLFW.GLFW_KEY_V);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        mc.player.setSprinting(true);
    }
}
