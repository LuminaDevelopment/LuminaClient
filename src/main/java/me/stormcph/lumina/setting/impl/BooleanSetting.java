package me.stormcph.lumina.setting.impl;

import com.google.gson.JsonObject;
import me.stormcph.lumina.setting.Setting;

public class BooleanSetting extends Setting {

    private boolean enabled;

    public BooleanSetting(String name, boolean defaultValue) {
        super(name);
        this.enabled = defaultValue;
    }

    public void toggle() {
        this.enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void save(JsonObject object) {
        object.addProperty(getName(), enabled);
    }

    @Override
    public void load(JsonObject object) {
        enabled = object.get(getName()).getAsBoolean();
    }
}
