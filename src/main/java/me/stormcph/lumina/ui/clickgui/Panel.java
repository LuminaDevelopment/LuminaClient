package me.stormcph.lumina.ui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.stormcph.lumina.ui.clickgui.impl.ModuleButton;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.ClickguiModule;
import me.stormcph.lumina.utils.Animation;
import me.stormcph.lumina.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel implements Component {

    private final Tab parent;
    private final Category category;
    private boolean visible;
    private final Animation animation, fadeAnim;
    private final List<ModuleButton> buttons = new ArrayList<>();

    public Panel(Tab parent, Category category) {
        this.parent = parent;
        this.category = category;

        this.visible = false;

        animation = new Animation(0, 0);
        fadeAnim = new Animation(0, 0);

        int offset = 25;
        for (ModuleButton button : buttons) {
            offset += button.getHeight();
        }

        animation.setEnd(50 + offset);
        visible = true;
        fadeAnim.setEnd(255);
        // <-

        buttons.clear();

        for(Module module : ModuleManager.INSTANCE.getModulesByCategory(category)) {
            buttons.add(new ModuleButton(this, module));
        }
    }

    @Override
    public void drawScreen(MatrixStack matrices, double mouseX, double mouseY) {

        float pX = parent.getX(), pY = parent.getY(), pW = parent.getWidth(), pH = parent.getHeight();

        animation.update(true);
        fadeAnim.update(true);

        drawRoundedRect(matrices, pX + 10, pY, pX + pW - 10, pY + pH + animation.getValue() - 10, 15, 20, new Color(20, 20, 20, 190));

        drawRect(matrices, pX + 30, pY + pH, pX + pW - 30, pY + pH + 3, new Color(100, 100, 100, (int) fadeAnim.getValue()).getRGB());
        double sf = 1f / getGuiScale();
        RenderUtils.enableScissor(pX * sf, (pY + pH) * sf, (pX + pW) * sf, (pY + pH + animation.getValue()) * sf);

        int offset = 25;
        for (ModuleButton button : buttons) {
            button.drawScreen(matrices, mouseX, mouseY, animation.getValue() - button.getHeight() - offset);
            offset += button.getHeight();
        }

        RenderSystem.disableScissor();

        if(animation.getEnd() != 0) {
            animation.setEnd(50 + offset);
        }

        if(animation.hasEnded() && animation.getEnd() == 0) {
            visible = false;
        }

        // FUN YAY SCISSOR BOX NOW :DDD (WONT TAKE ME 2 YEARS, TOTALLY) -> CONSULT HELP FROM OTHER CLIENT -> GET HELP FROM BADGPT:
        // Sample codi
        // noooo do i have to do it manually for evry gui scale i dont want pls nuuu
        // day #1 of suffering


      //  RenderUtils.enableScissor(0, 0, 1000, 1000);

        // Clipping zone
      //  drawRect(matrices, 0, 0, mc.getWindow().getWidth(), mc.getWindow().getHeight(), Color.red.getRGB());

    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        buttons.forEach(button1 -> button1.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        buttons.forEach(button1 -> button1.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        buttons.forEach(button1 -> button1.charTyped(chr, modifiers));
    }

    public void toggle() {

        if(buttons.isEmpty()) return;

        if(visible) {
            // TODO: change back when glscissor works
            animation.setEnd(0);
            fadeAnim.setEnd(0);
        }
        else {
            int offset = 25;
            for (ModuleButton button : buttons) {
                offset += button.getHeight();
            }

            animation.setEnd(50 + offset);
            visible = true;
            fadeAnim.setEnd(255);
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        buttons.forEach(button1 -> button1.keyPressed(keyCode, scanCode, modifiers));
    }

    public boolean isVisible() {
        return visible;
    }

    public Tab getParent() {
        return parent;
    }
}
