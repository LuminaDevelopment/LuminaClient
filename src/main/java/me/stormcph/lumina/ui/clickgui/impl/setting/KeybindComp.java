package me.stormcph.lumina.ui.clickgui.impl.setting;

import me.stormcph.lumina.ui.clickgui.impl.ModuleButton;
import me.stormcph.lumina.ui.clickgui.impl.SettingComp;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.KeybindSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class KeybindComp extends SettingComp {

    private boolean focused;

    public KeybindComp(Setting setting, ModuleButton parent) {
        super(setting, parent);
    }

    @Override
    public void drawScreen(DrawContext context, double mouseX, double mouseY, double y) {
        super.drawScreen(context, mouseX, mouseY, y);
        KeybindSetting setting = (KeybindSetting) getSetting();
        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();

        int middleY = (45 / 2) - (mc.textRenderer.fontHeight * getGuiScale()) / 2;
        drawString(context, setting.getName() + ": " + getKeyName(setting.getKey()), pX + 30, pY + pH + y + middleY, focused ? Color.white : Color.gray);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();
        focused = isInside(mouseX, mouseY, pX + 25, pY + pH + y, pX + pW - 25, pY + pH + y + 40) && button == 0;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {

    }

    @Override
    public void charTyped(char chr, int modifiers) {}

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if(focused) {
            KeybindSetting setting = (KeybindSetting) getSetting();
            setting.setKey(keyCode);
            focused = false;
        }
    }

    private String getKeyName(int key) {

        String keyName = GLFW.glfwGetKeyName(key, 0);

        if(key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            keyName = "RShift";
        }
        else if(key == GLFW.GLFW_KEY_LEFT_SHIFT) {
            keyName = "LShift";
        }
        else if(key == GLFW.GLFW_KEY_LEFT_CONTROL) {
            keyName = "LControl";
        }
        else if(key == GLFW.GLFW_KEY_RIGHT_CONTROL) {
            keyName = "RControl";
        }
        else if(key == GLFW.GLFW_KEY_RIGHT_ALT) {
            keyName = "RAlt";
        }
        else if(key == GLFW.GLFW_KEY_LEFT_ALT) {
            keyName = "LAlt";
        }
        else if(key == GLFW.GLFW_KEY_ESCAPE) {
            keyName = "None";
            key = 0;
        }
        else if(key == 0) {
            keyName = "None";
        }
        else if(keyName == null) {
            keyName = "None";
        }

        return keyName;
    }
}
