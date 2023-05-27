package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;

public class BetterHome extends Module {
    public BetterHome() {
        super("TitleScreen", "Modifies the title screen", Category.RENDER);
        this.setEnabled(true);
    }
}
