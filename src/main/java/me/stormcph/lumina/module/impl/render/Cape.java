package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;

public class Cape extends Module {

    private static final ModeSetting cape = new ModeSetting("cape", "lumina-b", "lumina-b", "lumina-w");

    public Cape() {
        super("Cape", "Forces a custom cape", Category.RENDER);
        addSettings(cape);
        this.toggle();
    }

    public static String getCurrentCape(){
        return cape.getMode();
    }

    // render code in AbstractClientPlayerEntityMixin.java

}
