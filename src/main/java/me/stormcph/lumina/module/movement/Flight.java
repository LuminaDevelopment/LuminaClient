package me.stormcph.lumina.module.movement;

import me.stormcph.lumina.module.Mod;
import me.stormcph.lumina.module.settings.NumberSettings;
import org.lwjgl.glfw.GLFW;

public class Flight extends Mod {

    public NumberSettings speed = new NumberSettings("Speed", 0, 10, 1, 1);
    public Flight() {
        super("Flight", "Vanilla fly module, instant ban with Anti-Cheat.", Category.MOVEMENT);
        addSettings(speed);
        //this.setKey(GLFW.GLFW_KEY_G);
    }

    @Override
    public void onTick() {
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlySpeed((float) speed.getValueFloat());
        super.onTick();
    }

    @Override
    public void onDisable() {
        mc.player.getAbilities().flying = false;
        super.onDisable();
    }
}
