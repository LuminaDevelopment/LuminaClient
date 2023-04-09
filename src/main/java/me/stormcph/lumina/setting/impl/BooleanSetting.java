package me.stormcph.lumina.setting.impl;

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
}
