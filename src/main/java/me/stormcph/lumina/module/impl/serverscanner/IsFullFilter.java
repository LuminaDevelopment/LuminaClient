package me.stormcph.lumina.module.impl.serverscanner;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;

public class IsFullFilter extends Module {
    public BooleanSetting value = new BooleanSetting("Is Full", false);

    public IsFullFilter() {
        super("IsFull", "Whether or not the server is full", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
    }
}
