package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Resizable;
import me.stormcph.lumina.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class TestRes extends Resizable {

    public TestRes() {
        super("TestRes", "fbwjjk", Category.RENDER, 50, 50, 50, 50, 0.1f, 2);
    }

    @Override
    public void draw(MatrixStack matrices) {
        startSize(matrices);
        RenderUtils.fill(matrices, getX() * (1 / getSize()), getY() * (1 / getSize()), getX() * (1 / getSize()) + 50, getY() * (1 / getSize()) + 50, -1);
        stopSize(matrices);
    }
}
