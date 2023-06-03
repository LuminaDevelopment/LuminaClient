package me.stormcph.lumina.module.impl.serverscanner;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.TextSetting;

public class PlayerCapFilter extends Module {
    public TextSetting value = new TextSetting("Value", "20");

    public PlayerCapFilter() {
        super("PlayerCap", "The server's player capacity", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
    }
}
