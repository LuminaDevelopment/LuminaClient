package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.RotationAxis;

public class Animations extends Module {

    private float start, end, current;

    public Animations() {
        super("Animations", "Old 1.7 sword swing", Category.RENDER);
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
                    if(end == -160) {
                        end = start;
                    }
                }

                float xo = 0.45f, yo = -0.4f, zo = 0;
                matrices.translate(xo, yo, zo);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(82));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(current));
                //                      z     x         y
                matrices.translate(0, 0.2 + xo, 0.4 - yo);
            }
        }
    }

    public void swing() {
        end = -160;
    }
}
