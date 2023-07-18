package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;

public class ESP extends Module {

    public static final ModeSetting mode = new ModeSetting("Mode", "Glow", "Glow");

    public ESP() {
        super("ESP", "See other people", Category.RENDER);
        addSettings(mode);
    }

}
