package me.stormcph.lumina.ui.screens.clickgui;

import me.stormcph.lumina.module.Module;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ModuleButton {

    public Module module;
    public Frame parent;
    public int offset;

    public ModuleButton(Module module, Frame parent, int offset) {
        this.module = module;
        this.parent = parent;
        this.offset = offset;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(31, 31, 31, 230).getRGB());
        if (isHovered(mouseX, mouseY)) DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(31, 31, 31, 230).getRGB());
        parent.mc.textRenderer.drawWithShadow(matrices, module.getName(), parent.x + 3, parent.y + offset + 5, module.isEnabled() ? Color.red.getRGB() : -1);
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == 0) {
                module.toggle();
            } else {

            }
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.x && mouseX < parent.x + parent.width && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
    }
}
