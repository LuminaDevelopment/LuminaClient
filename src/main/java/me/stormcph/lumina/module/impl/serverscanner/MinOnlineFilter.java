package me.stormcph.lumina.module.impl.serverscanner;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.TextSetting;

public class MinOnlineFilter extends Module {
    public TextSetting value = new TextSetting("Value", "0");

    public MinOnlineFilter() {
        super("MinOnline", "The minimum amount of players on the server", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
        noSave();
    }
}
