package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.KeyEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.utils.render.RenderUtils;
import me.stormcph.lumina.utils.render.SFUtils;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class Keystrokes extends HudModule implements SFUtils {

    private boolean w, a, s, d;

    public Keystrokes() {
        super("Keystrokes", "Render the keys you press on screen", Category.RENDER, 50, 50, 10, 10);
    }

    @Override
    public void draw(MatrixStack matrices) {
        w = mc.options.forwardKey.isPressed();
        s = mc.options.backKey.isPressed();
        d = mc.options.rightKey.isPressed();
        a = mc.options.leftKey.isPressed();
        int x = getX(), y = getY();

        //RenderUtils.fill(matrices, getX(), getY(), getX() + 50, getY() + 50, Color.white.getRGB());
        RenderUtils.fill(matrices, x + 50, y, x + 100, y + 50, new Color(200, 200, 200, w ? 200 : 100).getRGB());
        RenderUtils.fill(matrices, x, y + 50, x + 50, y + 100, new Color(200, 200, 200, a ? 200 : 100).getRGB());
        RenderUtils.fill(matrices, x + 50, y + 50, x + 100, y + 100, new Color(200, 200, 200, s ? 200 : 100).getRGB());
        RenderUtils.fill(matrices, x + 100, y + 50, x + 150, y + 100, new Color(200, 200, 200, d ? 200 : 100).getRGB());

        setWidth(150);
        setHeight(100);

    }

    @EventTarget
    public void onKeyPress(KeyEvent e) {
        if(e.getAction() == GLFW.GLFW_PRESS) {
            int key = e.getKey();
            if(key == mc.options.forwardKey.getDefaultKey().getCode()) {

            }
        }
    }
}
