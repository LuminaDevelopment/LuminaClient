package me.stormcph.lumina.module.impl.serverscanner;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.TextSetting;

import java.util.Date;

public class IPRangeFilter extends Module {
    public TextSetting value = new TextSetting("Value", "0.0.0.0/0");

    public IPRangeFilter() {
        super("IPRange", "The subnet of the ip of the server", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
        noSave();
    }
}
