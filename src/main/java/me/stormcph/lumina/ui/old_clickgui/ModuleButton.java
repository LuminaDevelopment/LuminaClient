package me.stormcph.lumina.ui.old_clickgui;

import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.ui.old_clickgui.setting.CheckBox;
import me.stormcph.lumina.ui.old_clickgui.setting.Component;
import me.stormcph.lumina.ui.old_clickgui.setting.ModeBox;
import me.stormcph.lumina.ui.old_clickgui.setting.Slider;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton {

    public Module module;
    public Frame parent;
    public int offset;

    public List<Component> components;

    public boolean extended;

    public ModuleButton(Module module, Frame parent, int offset) {
        this.module = module;
        this.parent = parent;
        this.offset = offset;
        this.extended = false;
        this.components = new ArrayList<>();

        int setOffset = parent.height;
        for (Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new CheckBox(setting, this, setOffset));
            } else if (setting instanceof ModeSetting) {
                components.add(new ModeBox(setting, this, setOffset));
            } else if (setting instanceof NumberSetting) {
                components.add(new Slider(setting, this, setOffset));
            }
            setOffset += parent.height;
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(31, 31, 31, 230).getRGB());
        if (isHovered(mouseX, mouseY)) DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(31, 31, 31, 230).getRGB());

        int textOffset = ((parent.height / 2) - parent.mc.textRenderer.fontHeight /2);
        parent.mc.textRenderer.drawWithShadow(matrices, module.getName(), parent.x + 3, parent.y + offset + 5, module.isEnabled() ? Color.red.getRGB() : -1);

        if (extended) {
            for (Component component : components) {
                component.render(matrices, mouseX, mouseY, delta);
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == 0) {
                module.toggle();
            } else if (button == 1) {
                extended = !extended;
                parent.updateButtons();
            }
        }

        for (Component component : components) {
            component.mouseClicked(mouseX, mouseY, button);
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.x && mouseX < parent.x + parent.width && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
    }
}
