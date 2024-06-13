package me.stormcph.lumina.ui.clickgui;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.impl.render.ClickguiModule;
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
            drawRoundedRect(matrices, x + 10, y, x + width - 10, y + height - 10, 15, 20, new Color(20, 20, 20, 190));
        }

        if(!ClickguiModule.performance.isEnabled()) {
            switch (category) {
                case COMBAT -> drawCategory(matrices, (x + 5) * (1 / scale), (y + 5) * (1 / scale), 0.08f, "textures/icons/combat.png");
                case MOVEMENT -> drawCategory(matrices, (x + 5) * (1 / scale), (y + 1) * (1 / scale), 0.027f, "textures/icons/move/png");
                case PLAYER -> drawCategory(matrices, (x + 15) * (1 / scale), (y + 5) * (1 / scale), 0.025f, "textures/icons/player.png");
                case RENDER -> drawCategory(matrices, (x + 15) * (1 / scale), (y + 10) * (1 / scale), 0.036f, "textures/icons/render.png");
                case MISC -> drawCategory(matrices, (x + 15) * (1 / scale), (y + 10) * (1 / scale), 0.024f, "textures/icons/misc.png");
                case GHOST -> drawCategory(matrices, (x + 15) * (1 / scale), (y + 3) * (1 / scale), 0.03f, "textures/icons/ghost.png");
            }
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
            panel.toggle();
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

    private void drawCategory(MatrixStack matrices, float x, float y, float scale, String texturePath) {
        matrices.push();
        matrices.scale(scale, scale, scale);
        drawImage(matrices, x, y, texturePath);
        matrices.pop();
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
