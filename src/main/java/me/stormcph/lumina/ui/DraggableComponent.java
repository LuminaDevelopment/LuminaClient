package me.stormcph.lumina.ui;

import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

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
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        // Enable dragging
        if(isInside(mouseX, mouseY, module.getX(), module.getY(), module.getX() + module.getWidth(), module.getY() + module.getHeight()) && button == 0) {
            dragging = true;
            dragX = (int) (mouseX - module.getX());
            dragY = (int) (mouseY - module.getY());
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        // Disable dragging
        if(button == 0) dragging = false;
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
