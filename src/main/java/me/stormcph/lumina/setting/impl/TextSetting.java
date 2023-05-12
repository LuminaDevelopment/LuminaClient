package me.stormcph.lumina.setting.impl;

import me.stormcph.lumina.setting.Setting;

public class TextSetting extends Setting {

    private String text;

    public TextSetting(String name, String text) {
        super(name);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
