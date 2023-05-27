package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import net.minecraft.util.math.RotationAxis;

public class Animations extends Module {

    private float start, end, current;

    private final NumberSetting xSetting, ySetting, zSetting;

    public static final ModeSetting mode = new ModeSetting("Mode", "Sword", "Sword", "Weapons", "All");

    public Animations() {
        super("Animations", "Old 1.7 sword swing", Category.RENDER);

        xSetting = new NumberSetting("X", 0.1, 3, 0.2, 0.1);
        ySetting = new NumberSetting("Y", 0.1, 1, 0.2, 0.1);
        zSetting = new NumberSetting("Z", 0.1, 1, 0.4, 0.1);
        addSettings(mode);
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
        if(nullCheck()) return;
        if(mc.options.useKey.isPressed()) {
            if(mc.options.attackKey.wasPressed()) {
                if(mc.targetedEntity != null && !mc.player.isBlocking()) mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
                swing();
            }
        }
    }

    public void render(ItemStack stack, MatrixStack matrices) {
        if(nullCheck()) return;
        float increment = 2;

        if(stack.getItem() instanceof SwordItem ||
                (stack.getItem() instanceof AxeItem && (mode.getMode().equals("Weapons") || mode.getMode().equals("All"))) ||
                (stack.getItem() instanceof ShovelItem && (mode.getMode().equals("All"))) ||
                (stack.getItem() instanceof PickaxeItem && (mode.getMode().equals("All"))) ||
                (stack.getItem() instanceof HoeItem && (mode.getMode().equals("All")))) {

            if (mc.options.useKey.isPressed()) {
                if (current != end) {
                    if (current > end) {
                        current -= increment;
                    } else if (current < end) {
                        current += increment;
                    }
                } else if (current == end) {
                    if (end == -130) {
                        end = start;
                    }
                }

                // Set the rotation point at the handle of the sword
                float xo = 0.45f, yo = -0.4f, zo = 0;
                matrices.translate(xo, yo, zo);
                // Rotates to face the player
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(82));
                // Rotates the sword up/down
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(current));

                // Use the setting values (removed by hades cuz it was being dumb you can add it back later  todo
                float x = (float) xSetting.getValue();
                float y = (float) (ySetting.getValue() + xo);
                float z = (float) (zSetting.getValue() - yo);

                matrices.translate(0, 0.2 + xo, 0.4 - yo);
            }
        }
    }

    public void swing() {
        end = -130;
    }
}
