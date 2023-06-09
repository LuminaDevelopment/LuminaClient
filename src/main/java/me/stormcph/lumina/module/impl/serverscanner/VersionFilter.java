package me.stormcph.lumina.module.impl.serverscanner;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.TextSetting;

public class VersionFilter extends Module {
    public TextSetting value = new TextSetting("Value", "");

    public VersionFilter() {
        super("Version", "The version of the server (uses regex)", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
        noSave();
    }
}
