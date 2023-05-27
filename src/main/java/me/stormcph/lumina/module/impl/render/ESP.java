package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.Render2DEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.utils.SFUtils;
import me.stormcph.lumina.utils.Vec3;

public class ESP extends Module implements SFUtils{

    public static final ModeSetting mode = new ModeSetting("Mode", "Glow", "Box", "2D", "Glow");
    private final Vec3 pos1 = new Vec3();
    private final Vec3 pos2 = new Vec3();
    private final Vec3 pos = new Vec3();

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

    private boolean checkCorner(double x, double y, double z, Vec3 min, Vec3 max) {
        pos.set(x, y, z);

        // Check Min
        if (pos.x < min.x) min.x = pos.x;
        if (pos.y < min.y) min.y = pos.y;
        if (pos.z < min.z) min.z = pos.z;

        // Check Max
        if (pos.x > max.x) max.x = pos.x;
        if (pos.y > max.y) max.y = pos.y;
        if (pos.z > max.z) max.z = pos.z;

        return false;
    }

}
