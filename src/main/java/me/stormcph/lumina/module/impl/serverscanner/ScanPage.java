package me.stormcph.lumina.module.impl.serverscanner;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.TextSetting;

public class ScanPage extends Module {
    public TextSetting value = new TextSetting("Value", "0");

    public ScanPage() {
        super("Page", "Skips forward to get different results", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
    }
}
