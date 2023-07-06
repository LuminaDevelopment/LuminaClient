package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;

public class NoHurtCam extends Module {
    public NoHurtCam() {
        super("NoHurtCam", "Disables the hurt camera effect", Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}
