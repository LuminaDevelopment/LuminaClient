package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.client.MinecraftClient;
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

    private long lastFrameTime = 0;

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

        MinecraftClient mc = MinecraftClient.getInstance();

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        if(!isValid(stack)) {
            return;
        }

        if (mc.options.useKey.isPressed()) {

            // Calculate the swing increment based on elapsed time
            float swingIncrement = 200f; // Adjust the swing speed as desired
            float swingProgress = swingIncrement * (elapsedTime / 1000f); // Divide by 1000 to convert milliseconds to seconds

            if (current > end) {
                current -= swingProgress;
            }
            else if (current < end) {
                current += swingProgress;
            }
            if (end == -130 && current <= end) {
                end = start;
            }
            if(end == start && current >= end) {
                current = end;
            }

            // Set the rotation point at the handle of the sword
            matrices.translate(0.4, -0.2, -0.1);
            // Rotates to face the player
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(85));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-17));
            // Rotates the sword up/down
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(current));

            matrices.translate(0, 0.6, 0.7);
        }
    }

    public static boolean isValid(ItemStack stack) {
        return (stack.getItem() instanceof SwordItem ||
                (stack.getItem() instanceof AxeItem && (Animations.mode.getMode().equals("Weapons") || Animations.mode.getMode().equals("All"))) ||
                (stack.getItem() instanceof ShovelItem && (Animations.mode.getMode().equals("All"))) ||
                (stack.getItem() instanceof PickaxeItem && (Animations.mode.getMode().equals("All"))) ||
                (stack.getItem() instanceof HoeItem && (Animations.mode.getMode().equals("All"))));
    }

    public void swing() {
        end = -130;
    }
}
