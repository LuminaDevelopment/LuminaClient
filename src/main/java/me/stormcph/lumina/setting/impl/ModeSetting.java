package me.stormcph.lumina.setting.impl;

import com.google.gson.JsonObject;
import me.stormcph.lumina.setting.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ModeSetting extends Setting {

    private String mode;
    private List<String> modes;
    private int index;

    public ModeSetting(String name, String defaultMode, String...modes) {
        super(name);
        this.mode = defaultMode;
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
    }

    public String getMode() {
        return mode;
    }

    public List<String> getModes() {
        return modes;
    }

    public void setMode(String mode) {
        this.mode = mode;
        this.index = modes.indexOf(mode);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        this.mode = modes.get(index);
    }

    public void cycle() {
        if(index < modes.size() - 1) {
            index++;
            mode = modes.get(index);
        }
        else if(index >= modes.size() -1) {
            index = 0;
            mode = modes.get(0);
        }
    }

    public boolean isMode(String mode) {
        return Objects.equals(this.mode, mode);
    }

    @Override
    public void save(JsonObject object) {
        object.addProperty(getName(), mode);
    }

    @Override
    public void load(JsonObject object) {
        mode = object.get(getName()).getAsString();
    }
}
