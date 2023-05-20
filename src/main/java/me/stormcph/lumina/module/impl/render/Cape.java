package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;

public class Cape extends Module {

    private static final ModeSetting cape = new ModeSetting("Texture", "cape-test", "cape-test");

    public Cape() {
        super("Cape", "Forces a custom cape", Category.RENDER);
    }

    public static String getCurrentCape(){
        return cape.getMode();
    }

    // render code in AbstractClientPlayerEntityMixin.java

}
