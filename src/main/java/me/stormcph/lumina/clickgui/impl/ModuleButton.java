package me.stormcph.lumina.clickgui.impl;

import me.stormcph.lumina.clickgui.Component;
import me.stormcph.lumina.clickgui.Panel;
import me.stormcph.lumina.clickgui.impl.setting.*;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.*;
import me.stormcph.lumina.utils.Animation;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Component.PanelComponent {

    private final Panel parent;
    private final Module module;
    private double y, height, oh;

    private final List<SettingComp> settings = new ArrayList<>();
    private final Animation expandAnim;
    private boolean expanded;

    public ModuleButton(Panel parent, Module module) {
        this.parent = parent;
        this.module = module;
        this.height = 55;
        this.oh = this.height;
        this.expanded = false;
        expandAnim = new Animation((int) height, (int) height);

        for (Setting setting : module.getSettings()) {
            if(setting instanceof BooleanSetting) {
                settings.add(new BoolComp(setting, this));
            }
            else if(setting instanceof ModeSetting) {
                settings.add(new ModeComp(setting, this));
            }
            else if(setting instanceof TextSetting) {
                settings.add(new TextComp(setting, this));
            }
            else if(setting instanceof KeybindSetting) {
                settings.add(new KeybindComp(setting, this));
            }
            else if(setting instanceof NumberSetting) {
                settings.add(new SliderComp(setting, this));
            }
        }
    }

    @Override
    public void drawScreen(MatrixStack matrices, double mouseX, double mouseY, double y) {
        this.y = y;

        expandAnim.update();
        this.height = expandAnim.getValue();

        float pX = parent.getParent().getX(), pY = parent.getParent().getY(), pW = parent.getParent().getWidth(), pH = parent.getParent().getHeight();

        drawRect(matrices, pX + 25, pY + pH + y, pX + pW - 25, pY + pH + y + height, new Color(40, 40, 40, 240).getRGB());
        drawString(matrices, module.getName() + (settings.isEmpty() ? "" : " ..."), pX + 30, pY + pH + y + 10, module.isEnabled() ? Color.GREEN : Color.white);

        if(isInside(mouseX, mouseY, pX + 25, pY + + pH + y, pX + pW - 25, pY + pH + y + oh)) {
            drawString(matrices, module.getDescription(),1, (mc.getWindow().getScaledHeight() * getGuiScale()) - mc.textRenderer.fontHeight - 10, Color.white);
        }

        if(expanded) {

            drawRect(matrices, pX + 25, pY + pH + y + oh - 5, pX + pW - 25, pY + pH + y + oh, Color.DARK_GRAY.getRGB());

            int offset = (int) oh;
            for (SettingComp setting : settings) {
                setting.drawScreen(matrices, mouseX, mouseY, y + offset);
                offset += 40;
            }
        }
    }


    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float pX = parent.getParent().getX(), pY = parent.getParent().getY(), pW = parent.getParent().getWidth(), pH = parent.getParent().getHeight();
        if(isInside(mouseX, mouseY, pX + 25, pY + + pH + y, pX + pW - 25, pY + pH + y + oh)) {
            if(button == 0) {
                module.toggle();
            }
            else if(button == 1) {
                if(expanded)  {
                    expanded = false;
                    expandAnim.setEnd((int) oh);
                }
                else {
                    if(settings.isEmpty()) return;
                    expanded = true;
                    expandAnim.setEnd((int) (oh + 5 + (settings.size() * 40)));
                }
            }
        }
        if(expanded) {
            for (SettingComp setting : settings) {
                setting.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if(expanded) {
            for (SettingComp setting : settings) {
                setting.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if(expanded) {
            for (SettingComp setting : settings) {
                setting.charTyped(chr, modifiers);
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if(expanded) {
            for (SettingComp setting : settings) {
                setting.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    public double getHeight() {
        return height;
    }

    public Panel getParent() {
        return parent;
    }
}
