package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.RotationAxis;

public class Animations extends Module {

    private float start, end, current;

    private NumberSetting xSetting, ySetting, zSetting;

    public Animations() {
        super("Animations", "Old 1.7 sword swing", Category.RENDER);

        xSetting = new NumberSetting("X", 0.1, 3, 0.2, 0.1);
        ySetting = new NumberSetting("Y", 0.1, 1, 0.2, 0.1);
        zSetting = new NumberSetting("Z", 0.1, 1, 0.4, 0.1);
        addSettings(xSetting, ySetting, zSetting);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        start = -90;
        end = start;
        current = start;
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(mc.options.useKey.isPressed()) {
            if(mc.options.attackKey.isPressed()) swing();
        }
    }

    public void render(ItemStack stack, MatrixStack matrices) {

        float increment = 2;

        if(stack.getItem() instanceof SwordItem) {
            if(mc.options.useKey.isPressed()) {
                if(current != end) {
                    if(current > end) {
                        current -= increment;
                    }
                    else if(current < end) {
                        current += increment;
                    }
                }
                else if(current == end) {
                    if(end == -130) {
                        end = start;
                    }
                }

                float xo = 0.45f, yo = -0.4f, zo = 0;
                matrices.translate(xo, yo, zo);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(82));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(current));

                // Use the setting values
                float x = (float) xSetting.getValue();
                float y = (float) (ySetting.getValue() + xo);
                float z = (float) (zSetting.getValue() - yo);

                matrices.translate(x, y, z);
            }
        }
    }

    public void swing() {
        end = -130;
    }
}
