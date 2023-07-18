package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;

public class AutoRefill extends Module {
    public AutoRefill() {
        super("AutoRefill", "refills certain items in hotbar from inventory", Category.GHOST);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}
