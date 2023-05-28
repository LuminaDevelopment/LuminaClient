package me.stormcph.lumina.module;

import net.minecraft.client.util.math.MatrixStack;

public abstract class HudModule extends Module {

    private int x, y, width, height;
    private float scaleX, scaleY;
    public HudModule(String name, String description, Category category, int x, int y, int width, int height) {
        super(name, description, category);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        scaleX = 1;
        scaleY = 1;
    }

    public abstract void draw(MatrixStack matrices);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return (int) (width * scaleX);
    }

    public int getHeight() {
        return (int) (height * scaleY);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }
}
