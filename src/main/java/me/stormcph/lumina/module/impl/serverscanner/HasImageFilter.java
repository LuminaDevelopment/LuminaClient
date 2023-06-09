package me.stormcph.lumina.module.impl.serverscanner;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;

public class HasImageFilter extends Module {
    public BooleanSetting value = new BooleanSetting("Has Image", false);

    public HasImageFilter() {
        super("HasImage", "Whether or not the server has a custom thumbnail image", Category.SERVER_SCANNER);
        addSettings(value);
        removeKeybind();
        noSave();
    }
}
