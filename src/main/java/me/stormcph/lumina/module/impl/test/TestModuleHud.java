package me.stormcph.lumina.module.impl.test;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.utils.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class TestModuleHud extends HudModule {

    public TestModuleHud() {
        super("TestHudMod", "bwijegmw", Category.RENDER, 50, 50, 100, 50);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        setScaleX(1);
        setScaleY(1);
    }

    @Override
    public void draw(MatrixStack matrices) {
        RenderUtils.fill(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), Color.black.getRGB());
    }
}
