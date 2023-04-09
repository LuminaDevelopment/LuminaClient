package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;

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
}
