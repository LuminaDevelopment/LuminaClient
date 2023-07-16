package me.stormcph.lumina.ui.old_ui;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.ui.old_ui.setting.Component;
import me.stormcph.lumina.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

public class Frame {

    public int x, y, width, height, dragX, dragY;
    public Category category;

    public boolean dragging, extended;

    private List<ModuleButton> buttons;

    protected MinecraftClient mc = MinecraftClient.getInstance();

    private BufferedImage gradientImage;

    public Frame(Category category, int x, int y, int width, int height) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.extended = false;

        buttons = new ArrayList<>();

        int offset = height;
        for (Module mod : ModuleManager.INSTANCE.getModulesByCategory(category)) {
            buttons.add(new ModuleButton(mod, this, offset));
            offset += height;
        }

        createGradientImage();
    }

    private Identifier bufferedImageToTexture(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nativeImage.setColor(x, y, bufferedImage.getRGB(x, y));
            }
        }
        NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
        return MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("gradient_frame", texture);
    }


    private void createGradientImage() {
        Color startColor = new Color(173, 143, 97); // Light blue
        Color endColor = new Color(255, 0, 133); // Dark-ish purple
        GradientPaint gradientPaint = new GradientPaint(0, 0, startColor, 0, height, endColor);
        gradientImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = gradientImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
    }

    public void setX(int x){
        this.x = x;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier gradientTexture = bufferedImageToTexture(gradientImage);
        context.drawTexture(gradientTexture, x, y, 0, 0, width, height, width, height);

        RenderUtils.drawStringShadow(context, category.name, x + 12, y + 4, -1);
        RenderUtils.drawStringShadow(context, category.name, x + 12, y + 4, -1);
        RenderUtils.drawStringShadow(context, extended ? "∨" : ">", x + width - 12 - mc.textRenderer.getWidth("+"), y + 4, -1);

        if (extended) {
            for (ModuleButton button : buttons) {
                button.render(context, mouseX, mouseY, delta);
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == 0) {
                dragging = true;
                dragX = (int) (mouseX - x);
                dragY = (int) (mouseY - y);
            } else if (button == 1) {
                extended = !extended;
            }
        }

        if (extended) {
            for (ModuleButton mb : buttons) {
                mb.mouseClicked(mouseX, mouseY, button);
            }
        }
    }



    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging == true) dragging = false;

        for (ModuleButton mc : buttons) {
            mc.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void updatePosition(double mouseX, double mouseY) {
        if (dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }

    public void updateButtons() {
        int offset = height;

        for (ModuleButton button : buttons) {
            button.offset = offset;
            offset += height;

            if (button.extended) {
                for (Component component : button.components) {
                    if (component.setting.isVisible()) offset += height;
                }
            }
        }
    }
}

