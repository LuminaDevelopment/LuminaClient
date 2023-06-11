package me.stormcph.lumina.module;

import net.minecraft.client.util.math.MatrixStack;

public abstract class Resizable extends HudModule{

    private final float minSize, maxSize;
    private float currentSize;

    public Resizable(String name, String description, Category category, int x, int y, int width, int height, float minSize, float maxSize) {
        super(name, description, category, x, y, width, height);
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    protected void startSize(MatrixStack matrices) {
        matrices.push();
        matrices.scale(getSize(), getSize(), 0);
    }

    protected void stopSize(MatrixStack matrices) {
        matrices.pop();
    }

    public float getMaxSize() {
        return maxSize;
    }

    public float getMinSize() {
        return minSize;
    }

    public float getSize() {
        return currentSize;
    }

    public void setSize(float currentSize) {
        this.currentSize = currentSize;
    }
}
