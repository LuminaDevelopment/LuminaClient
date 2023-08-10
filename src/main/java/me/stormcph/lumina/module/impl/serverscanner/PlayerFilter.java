package me.stormcph.lumina.module.impl.serverscanner;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.TextSetting;

public class PlayerFilter extends Module {
    public TextSetting value = new TextSetting("Value", "");

    public PlayerFilter() {
        super("Player", "The name of a player on the server", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
        noSave();
    }
}
