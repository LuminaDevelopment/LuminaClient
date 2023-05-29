package me.stormcph.lumina.module.impl.serverscanner;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.TextSetting;

import java.util.Date;

public class SeenAfterFilter extends Module {
    public TextSetting value = new TextSetting("Value", "" + ((new Date()).getTime() / 1000));

    public SeenAfterFilter() {
        super("SeenAfter", "The earliest the server could have last been seen (unix timestamp)", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
    }
}
