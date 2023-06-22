package me.stormcph.lumina.ui;

import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.module.Resizable;
import me.stormcph.lumina.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

public class DraggableComponent {

    // The Hud Module to work with
    private final HudModule module;
    // Minecraft Client reference
    private final MinecraftClient mc;

    // If you are dragging it
    private boolean dragging;
    // Drag coordinates
    private double dragX, dragY;

    public DraggableComponent(HudModule module) {
        this.module = module;
        this.mc = MinecraftClient.getInstance();
        // Set dragging to false default
        dragging = false;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY) {
        // Set the new position when dragging
        if(dragging) {
            int newX = (int) (mouseX - dragX);
            int newY = (int) (mouseY - dragY);

            // Make sure you're not moving it off the screen
            if(newX >= 0 && newX + module.getWidth() <= mc.getWindow().getScaledWidth()) {
                module.setX(newX);
            }

            if (newY >= 0  && newY + module.getHeight() <= mc.getWindow().getScaledHeight()) {
                module.setY(newY);
            }

            // Only drag one component at a time
            HudConfigScreen.components.forEach(c -> {
                if (c != this) c.setDragging(false);
            });
        }

        // Draw the outline
        RenderUtils.drawHollowRect(matrices, module.getX(), module.getY(), module.getWidth(), module.getHeight(), new Color(0, 255, 255).getRGB(), 1);

        if(module instanceof Resizable) {
            RenderUtils.fill(matrices, module.getX(), module.getY() + module.getHeight() + 20, module.getX() + 10, module.getY() + module.getHeight() + 30, Color.green.getRGB());
            RenderUtils.fill(matrices, module.getX() + 20, module.getY() + module.getHeight() + 20, module.getX() + 30, module.getY() + module.getHeight() + 30, Color.red.getRGB());
        }

       // RenderUtils.fill(matrices, module.getX() + module.getWidth(), module.getY() + module.getHeight(), module.getX() + module.getWidth() + 20, module.getY() + module.getHeight() + 20, new Color(0, 255, 255, 100).getRGB());
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        // Enable dragging
        if(isInside(mouseX, mouseY, module.getX(), module.getY(), module.getX() + module.getWidth(), module.getY() + module.getHeight()) && button == 0) {
            dragging = true;
            dragX = (int) (mouseX - module.getX());
            dragY = (int) (mouseY - module.getY());
        }
        else if(isInside(mouseX, mouseY, module.getX(), module.getY() + module.getHeight() + 20, module.getX() + 10, module.getY() + module.getHeight() + 30) && button == 0) {
           if(!(module instanceof Resizable resizable)) return;
           float current = resizable.getSize();
           if(current == resizable.getMaxSize()) return;
           resizable.setSize(current + 0.1f);
        }
        else if(isInside(mouseX, mouseY, module.getX() + 20, module.getY() + module.getHeight() + 20, module.getX() + 30, module.getY() + module.getHeight() + 30) && button == 0) {
            if(!(module instanceof Resizable resizable)) return;
            float current = resizable.getSize();
            if(current == resizable.getMinSize()) return;
            resizable.setSize(current - 0.1f);
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        // Disable dragging
        if(button == 0) {
            dragging = false;
        }
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {

    }

    public HudModule getModule() {
        return module;
    }

    private boolean isInside(double mouseX, double mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}
