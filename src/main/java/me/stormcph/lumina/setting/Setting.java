package me.stormcph.lumina.setting;

import com.google.gson.JsonObject;

public abstract class Setting {

    private final String name;
    private boolean visible = true;

    public Setting(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public abstract void save(JsonObject object);

    public abstract void load(JsonObject object);
}
