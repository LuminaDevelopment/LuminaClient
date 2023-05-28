package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.Render2DEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.utils.SFUtils;

public class ESP extends Module implements SFUtils{

    public static final ModeSetting mode = new ModeSetting("Mode", "Glow", "Box", "2D", "Glow");

    public ESP() {
        super("ESP", "See other people", Category.RENDER);
        addSettings(mode);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

        if(nullCheck()) return;


        switch (mode.getMode()) {
            case "Box" -> {

            }
            case "2D" -> {

            }
        }

    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {

    }

}
