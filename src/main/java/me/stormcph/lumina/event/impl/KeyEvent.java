package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class KeyEvent extends Event {

    private final int action;
    private int key;

    public KeyEvent(int key, int action) {
        this.key = key;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getAction() {
        return action;
    }

    public boolean matches(KeyBinding keyBinding) {
        return keyBinding.matchesKey(this.key, GLFW.GLFW_KEY_UNKNOWN);
    }
}
