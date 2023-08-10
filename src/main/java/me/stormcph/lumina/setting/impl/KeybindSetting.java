package me.stormcph.lumina.setting.impl;

import com.google.gson.JsonObject;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.Setting;

public class KeybindSetting extends Setting {

    private int keyCode;
    private final Module m;

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

    public Module getModule() {
        return m;
    }

    @Override
    public void save(JsonObject object) {
        object.addProperty(getName(), keyCode);
    }

    @Override
    public void load(JsonObject object) {
        keyCode = object.get(getName()).getAsInt();
    }
}
