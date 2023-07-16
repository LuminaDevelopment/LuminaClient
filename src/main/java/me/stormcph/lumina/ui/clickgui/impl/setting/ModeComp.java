package me.stormcph.lumina.ui.clickgui.impl.setting;

import me.stormcph.lumina.ui.clickgui.impl.ModuleButton;
import me.stormcph.lumina.ui.clickgui.impl.SettingComp;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.ModeSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class ModeComp extends SettingComp {

    public ModeComp(Setting setting, ModuleButton parent) {
        super(setting, parent);
    }

    @Override
    public void drawScreen(DrawContext context, double mouseX, double mouseY, double y) {
        super.drawScreen(context, mouseX, mouseY, y);
        ModeSetting setting = (ModeSetting) getSetting();
        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();

      //  drawRect(matrices, pX + 25, pY + pH + y, pX + pW - 25, pY + pH + y + 40, Color.DARK_GRAY.darker().getRGB());
        drawString(context, setting.getName() + ":" + setting.getMode(), pX + 30, pY + pH + y + 10, Color.white);

    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();
        if(isInside(mouseX, mouseY, pX + 25, pY + pH + y, pX + pW - 25, pY + pH + y + 40) && button == 0) {
            ModeSetting setting = (ModeSetting) getSetting();
            setting.cycle();
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {

    }

    @Override
    public void charTyped(char chr, int modifiers) {

    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {

    }
}
