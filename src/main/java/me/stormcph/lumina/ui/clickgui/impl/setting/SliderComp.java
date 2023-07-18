package me.stormcph.lumina.ui.clickgui.impl.setting;

import me.stormcph.lumina.ui.clickgui.impl.ModuleButton;
import me.stormcph.lumina.ui.clickgui.impl.SettingComp;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderComp extends SettingComp {

    public NumberSetting numSet;

    private boolean sliding = false;

    public SliderComp(Setting setting, ModuleButton parent) {
        super(setting, parent);
        this.numSet = (NumberSetting)setting;
    }

    @Override
    public void drawScreen(DrawContext context, double mouseX, double mouseY, double y) {
        super.drawScreen(context, mouseX, mouseY, y);

        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();

        double diff = Math.min(pW - 31, Math.max(0, mouseX * getGuiScale() - (pX + 30)));
        int renderWidth = (int) ((pW - 61) * (numSet.getValue() - numSet.getMin()) / (numSet.getMax() - numSet.getMin()));


        drawRect(context, pX + 30, pY + pH + y,pX + 30 + renderWidth, pY + y + pH + 45, Color.green.darker().getRGB());

        if (sliding) {
            if (diff == 0 ) {
                numSet.setValue(numSet.getMin());
            } else {
                numSet.setValue(roundToPlace(((diff / (pW - 31)) * (numSet.getMax() - numSet.getMin()) + numSet.getMin()), 2));
            }
        }

        int middleY = (45 / 2) - (mc.textRenderer.fontHeight * getGuiScale()) / 2;
        drawString(context, numSet.getName() + ": " + roundToPlace(numSet.getValue(), 2),pX + 37, pY + pH + y + middleY, Color.white);

       // drawRect(matrices, pX + 30, pY + pH + y,pX + pW - 30, pY + y + pH + 50, Color.red.getRGB());
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();
        if (isInside(mouseX, mouseY, pX + 30, pY + pH + y,pX + pW - 31, pY + y + pH + 50)) {
            if(button == 0) sliding = true;
            else if(button == 1) numSet.setValue(numSet.getDefaultValue());
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        sliding = false;
    }

    @Override
    public void charTyped(char chr, int modifiers) {

    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {

    }

    private double roundToPlace(double value, int place) {
        if (place < 0) {
            return value;
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(place, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
