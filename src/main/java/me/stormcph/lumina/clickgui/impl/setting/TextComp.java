package me.stormcph.lumina.clickgui.impl.setting;

import me.stormcph.lumina.clickgui.impl.ModuleButton;
import me.stormcph.lumina.clickgui.impl.SettingComp;
import me.stormcph.lumina.setting.Setting;
import me.stormcph.lumina.setting.impl.TextSetting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TextComp extends SettingComp {

    private boolean focused;
    private String text;

    public TextComp(Setting setting, ModuleButton parent) {
        super(setting, parent);
        this.text = ((TextSetting) setting).getText();
    }

    @Override
    public void drawScreen(MatrixStack matrices, double mouseX, double mouseY, double y) {
        super.drawScreen(matrices, mouseX, mouseY, y);
        TextSetting setting = (TextSetting) getSetting();
        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();

       // drawRect(matrices, pX + 25, pY + pH + y, pX + pW - 25, pY + pH + y + 40, Color.green.getRGB());
        drawString(matrices, setting.getName() + ":" + text, pX + 30, pY + pH + y + 10, focused ? Color.white : Color.gray);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        float pX = parent.getParent().getParent().getX(), pY = parent.getParent().getParent().getY(), pW = parent.getParent().getParent().getWidth(), pH = parent.getParent().getParent().getHeight();
        focused = isInside(mouseX, mouseY, pX + 25, pY + pH + y, pX + pW - 25, pY + pH + y + 40) && button == 0;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {

    }

    private final List<Character> chars = new ArrayList<>();

    @Override
    public void charTyped(char chr, int modifiers) {}

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        TextSetting setting = (TextSetting) getSetting();
        if(focused) {

            if(keyCode == GLFW.GLFW_KEY_ENTER) {
                focused = false;
            }
            if(keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if(!chars.isEmpty()) {
                    int index = chars.size() - 1;
                    chars.remove(index);

                    StringBuilder sb = new StringBuilder();

                    for (Character c : chars) {
                        sb.append(c);
                    }

                    text = sb.toString();
                }
            }
            else {
                if(keyCode == GLFW.GLFW_KEY_LEFT_CONTROL || keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL || keyCode == 15 || keyCode == 58 || keyCode == 56 || keyCode == 219 || keyCode == 203 || keyCode == 200 || keyCode == 208 || keyCode == 205 || keyCode == 210 || keyCode == 199 || keyCode == 184) {
                    return;
                }

                String keyName = GLFW.glfwGetKeyName(keyCode, keyCode);

               if(keyCode == 32) {
                   keyName = " ";
               }

                if(keyName != null) {
                    if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                        keyName = keyName.toUpperCase(Locale.ROOT);
                    }
                    char typedChar = keyName.charAt(0);
                    addChar(typedChar);
                }
            }

            setting.setText(text);
        }
    }

    private void addChar(char typedChar) {
        chars.add(typedChar);

        StringBuilder sb = new StringBuilder();

        for(Character c : chars) {
            sb.append(c);
        }

        text = sb.toString();
    }
}
