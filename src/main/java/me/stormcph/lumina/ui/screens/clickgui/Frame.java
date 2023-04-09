package me.stormcph.lumina.ui.screens.clickgui;

import me.stormcph.lumina.module.Mod;
import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
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
    public Mod.Category category;

    public boolean dragging, extended;

    private List<ModuleButton> buttons;

    protected MinecraftClient mc = MinecraftClient.getInstance();

    private BufferedImage gradientImage;

    public Frame(Mod.Category category, int x, int y, int width, int height) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.extended = false;

        buttons = new ArrayList<>();

        int offset = height;
        for (Mod mod : ModuleManager.ISTANCE.getModulesInCategory(category)) {
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

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Identifier gradientTexture = bufferedImageToTexture(gradientImage);
        RenderSystem.setShaderTexture(0, gradientTexture);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, width, height, width, height);

        mc.textRenderer.drawWithShadow(matrices, category.name, x + 12, y + 4, -1);
        mc.textRenderer.drawWithShadow(matrices, extended ? "∨" : ">", x + width - 12 - mc.textRenderer.getWidth("+"), y + 4, -1);

        if (extended) {
            for (ModuleButton button : buttons) {
                button.render(matrices, mouseX, mouseY, delta);
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
}

