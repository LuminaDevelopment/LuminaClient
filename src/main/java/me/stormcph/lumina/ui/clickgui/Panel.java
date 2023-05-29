package me.stormcph.lumina.ui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.stormcph.lumina.ui.clickgui.impl.ModuleButton;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.ClickguiModule;
import me.stormcph.lumina.utils.Animation;
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

        // TODO: change back to false wehn glscissor is fixed
        this.visible = true;

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

        drawRoundedRect(matrices, pX, pY, pX + pW, pY + pH + animation.getValue(), 15, 20, new Color(20, 20, 20, 190));
        drawRect(matrices, pX + 30, pY + pH, pX + pW - 30, pY + pH + 3, new Color(100, 100, 100, (int) fadeAnim.getValue()).getRGB());

        int offset = 25;
        for (ModuleButton button : buttons) {
            button.drawScreen(matrices, mouseX, mouseY, animation.getValue() - button.getHeight() - offset);
            offset += button.getHeight();
        }

        if(animation.getEnd() != 0) {
            animation.setEnd(50 + offset);
        }

        if(animation.hasEnded() && animation.getEnd() == 0) {
            visible = false;
        }

        if(!ClickguiModule.performance.isEnabled()) {
            switch (category) {
                case COMBAT -> {
                    matrices.push();
                    float scale = 0.08f;
                    matrices.scale(scale, scale, scale);
                    drawImage(matrices, (pX + 5) * (1 / scale), (pY + 5) * (1 / scale), "textures/icons/combat.png");
                    matrices.pop();
                }
                case MOVEMENT -> {
                    matrices.push();
                    float scale = 0.027f;
                    matrices.scale(scale, scale, scale);
                    drawImage(matrices, (pX + 5) * (1 / scale), (pY + 1) * (1 / scale), "textures/icons/move.png");
                    matrices.pop();
                }
                case PLAYER -> {
                    matrices.push();
                    float scale = 0.025f;
                    matrices.scale(scale, scale, scale);
                    drawImage(matrices, (pX + 15) * (1 / scale), (pY + 5) * (1 / scale), "textures/icons/player.png");
                    matrices.pop();
                }
                case RENDER -> {
                    matrices.push();
                    float scale = 0.036f;
                    matrices.scale(scale, scale, scale);
                    drawImage(matrices, (pX + 15) * (1 / scale), (pY + 10) * (1 / scale), "textures/icons/render.png");
                    matrices.pop();
                }
                case MISC -> {
                    matrices.push();
                    float scale = 0.024f;
                    matrices.scale(scale, scale, scale);
                    drawImage(matrices, (pX + 15) * (1 / scale), (pY + 10) * (1 / scale), "textures/icons/misc.png");
                    matrices.pop();
                }
                case GHOST -> {
                    matrices.push();
                    float scale = 0.03f;
                    matrices.scale(scale, scale, scale);
                    drawImage(matrices, (pX + 15) * (1 / scale), (pY + 3) * (1 / scale), "textures/icons/ghost.png");
                    matrices.pop();
                }
            }
        }

        // FUN YAY SCISSOR BOX NOW :DDD (WONT TAKE ME 2 YEARS, TOTALLY) -> CONSULT HELP FROM OTHER CLIENT -> GET HELP FROM BADGPT:
        // Sample codi
        // noooo do i have to do it manually for evry gui scale i dont want pls nuuu
        // day #1 of suffering



        scissor(pX, pY + pH, pW, pH + animation.getValue());
       // drawRect(matrices, 0, 0, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), Color.white.getRGB());
        RenderSystem.disableScissor();
    }


    private void scissor(float x, float y, float width, float height) {
        // Calculate the dimensions of the scissor box in screen coordinates
        int guiScale = (int) mc.getWindow().getScaleFactor();

        switch (guiScale) {
            case 1: {
                int scaledX = (int) (x / guiScale);
                int scaledY = (int) (mc.getWindow().getScaledHeight() - ((y + height) / guiScale));
                int scaledWidth = (int) (width / guiScale);
                int scaledHeight = (int) (height / guiScale);

                // Enable the scissor test
                RenderSystem.enableScissor(scaledX, scaledY, scaledWidth, scaledHeight);
                break;
            }
            case 2: {
                RenderSystem.enableScissor(/* Screw you */(int) x, (int) -y, (int) (mc.getWindow().getFramebufferWidth() - x), (int) (mc.getWindow().getFramebufferHeight()));
                break;
            }
            case 3: {
                break;
            }
            case 4: {
                break;
            }
            default: {
                System.out.println("How the fuck did you manage this");
                break;
            }
        }
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
           // animation.setEnd(0);
            //fadeAnim.setEnd(0);
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
