package me.stormcph.lumina.clickgui;

import me.stormcph.lumina.module.Category;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class Tab implements Component {

    private final Category category;
    private float x, y, width, height;
    private boolean dragging;
    private float dragX, dragY;
    private final Panel panel;

    public Tab(Category category, float x) {
        this.category = category;
        this.x = x;
        this.y = 20;
        this.width = 260;
        this.height = 50;
        panel = new Panel(this, category);
    }

    @Override
    public void drawScreen(MatrixStack matrices, double mouseX, double mouseY) {
        float sf = (float) getGuiScale();
        if(dragging) {
            float xDiff = (float) (mouseX - dragX);
            float yDiff = (float) (mouseY - dragY);
            x = xDiff * sf;
            y = yDiff * sf;
        }

        if(panel.isVisible()) {
            panel.drawScreen(matrices, mouseX, mouseY);
        }
        else {
            // TODO: change back when glscissor works
            //drawRoundedRect(matrices, x, y, x + width, y + height, 15, 20, new Color(20, 20, 20, 190));
        }

        float textX = (x + (width / 2)) - (mc.textRenderer.getWidth(category.name)) - 15;
        drawString(matrices, category.name, textX, y + 10f, Color.white);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float sf = (float) getGuiScale();
        if(isInside(mouseX, mouseY, x, y, x + width, y + height) && !dragging && button == 0) {
            dragging = true;
            dragX = (int) ((mouseX - x / sf));
            dragY = (int) ((mouseY - y / sf));
        }

        if(isInside(mouseX, mouseY, x, y, x + width, y + height) && button == 1) {
            // TODO: change back when glscissor works
            //panel.toggle();
        }

        if(panel.isVisible()) {
            panel.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if(button == 0) {
            dragging = false;
        }

        if(panel.isVisible()) {
            panel.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if(panel.isVisible()) {
            panel.charTyped(chr, modifiers);
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if(panel.isVisible()) {
            panel.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }
}
