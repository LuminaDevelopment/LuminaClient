package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;

public class FullBright extends Module {
    NumberSetting brightness = new NumberSetting("Brightness", 0, 15, 15, 1);
    int lastBrightness = 0;
    public FullBright() {
        super("Fullbright", "no more shadows!", Category.RENDER);
        addSettings(brightness);
    }

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        super.onEnable();
        mc.worldRenderer.reload();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (lastBrightness != brightness.getIntValue()) mc.worldRenderer.reload();
        lastBrightness = brightness.getIntValue();
    }

    @Override
    public void onDisable() {
        if(nullCheck()) return;
        super.onDisable();
    }

    public int getBrightness() {
        return brightness.getIntValue();
    }
}