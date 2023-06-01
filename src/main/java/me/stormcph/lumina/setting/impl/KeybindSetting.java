package me.stormcph.lumina.setting.impl;

import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.Setting;

public class KeybindSetting extends Setting {

    private int keyCode;
    private Module m;

    public KeybindSetting(String name, int keyCode, Module m) {
        super(name);
        this.keyCode = keyCode;
         this.m = m;
    }

    public int getKey() {
        return keyCode;
    }

    public void setKey(int keyCode) {
        this.keyCode = keyCode;
        m.setKey(keyCode);
    }

    public void setting$setKey(int keyCode) {
        this.keyCode = keyCode;
    }

    public Module getModule() {
        return m;
    }
}
