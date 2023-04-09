package me.stormcph.lumina.module.settings;

public class Setting {

    private String name;
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
}
