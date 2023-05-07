package me.stormcph.lumina.module.impl.movement;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PlayerMoveEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import org.lwjgl.glfw.GLFW;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Keeps your sprint", Category.MOVEMENT);
        //this.setKey(GLFW.GLFW_KEY_V);
    }

    @EventTarget
    public void onMove(PlayerMoveEvent e) {
        if(mc.player.forwardSpeed > 0 && !mc.player.isUsingItem() && !mc.player.isSneaking()) {
            mc.player.setSprinting(true);
        }
    }
}
