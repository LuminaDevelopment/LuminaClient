package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

import java.awt.*;

public class TargetHUD extends HudModule {
    LivingEntity target;
    public TargetHUD() {
        super("TargetHud", "funny gui that shows target info", Category.RENDER, 100, 100, 100, 40);
    }

    @Override
    public void draw(MatrixStack matrices) {
        renderTargetHud(matrices);
    }

    private void renderTargetHud(MatrixStack matrices) {
        TextRenderer renderer = mc.textRenderer;

        target = (LivingEntity) mc.targetedEntity;

        if(target != null) {
            renderer.drawWithShadow(matrices, target.getName(), getX(), (float) (0 + 1) + getY(), new Color(236, 133, 209).getRGB());
            renderer.drawWithShadow(matrices, String.valueOf((int)target.getHealth() + "\u2764"), getX() + 80, getY() + 30, new Color(236, 133, 209).getRGB());
        }
    }
}
