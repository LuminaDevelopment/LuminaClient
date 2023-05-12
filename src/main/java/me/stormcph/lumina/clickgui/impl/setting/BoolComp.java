package me.stormcph.lumina.clickgui.impl.setting;

import me.stormcph.lumina.clickgui.impl.ModuleButton;
import me.stormcph.lumina.clickgui.impl.SettingComp;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class BoolComp extends SettingComp {

    public BoolComp(Setting setting, ModuleButton parent) {
        super(setting, parent);
    }

    @Override
    public void drawScreen(MatrixStack matrices, double mouseX, double mouseY, double y) {
        super.drawScreen(matrices, mouseX, mouseY, y);
        BooleanSetting setting = (BooleanSetting) getSetting();
        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();

       // drawRect(matrices, pX + 25, pY + pH + y, pX + pW - 25, pY + pH + y + 40, Color.DARK_GRAY.darker().getRGB());
        drawString(matrices, setting.getName(), pX + 30, pY + pH + y + 10, setting.isEnabled() ? Color.white : Color.GRAY);

    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();
        if(isInside(mouseX, mouseY, pX + 25, pY + pH + y, pX + pW - 25, pY + pH + y + 40) && button == 0) {
            BooleanSetting setting = (BooleanSetting) getSetting();
            setting.toggle();
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
