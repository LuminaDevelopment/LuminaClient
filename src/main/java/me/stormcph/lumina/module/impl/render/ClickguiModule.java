package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.ModeSetting;

public class ClickguiModule extends Module {

    public static BooleanSetting performance = new BooleanSetting("Performance", true);
    public static ModeSetting clickguiMode = new ModeSetting("Clickgui Mode", "New", "New", "Old");

    public ClickguiModule() {
        super("Clickgui", "Opens the module selection/editing screen", Category.RENDER);
        this.addSettings(performance, clickguiMode);
    }
}
