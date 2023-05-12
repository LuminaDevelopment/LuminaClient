package me.stormcph.lumina.clickgui;

import me.stormcph.lumina.utils.SFUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public interface Component extends SFUtils {

    MinecraftClient mc = MinecraftClient.getInstance();
    void drawScreen(MatrixStack matrices, double mouseX, double mouseY);
    void mouseClicked(double mouseX, double mouseY, int button);
    void mouseReleased(double mouseX, double mouseY, int button);
    void charTyped(char chr, int modifiers);
    void keyPressed(int keyCode, int scanCode, int modifiers);

    default boolean isInside(double mouseX, double mouseY, double x, double y, double x2, double y2) {
        double sf = MinecraftClient.getInstance().getWindow().getScaleFactor();
        x /= sf;
        y = y / sf;
        x2 = x2 / sf;
        y2 = y2 / sf;

        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    public abstract class PanelComponent implements SFUtils, Component {

        public abstract void drawScreen(MatrixStack matrices, double mouseX, double mouseY, double y);

        /**
         * Unused
          */
        @Override
        public void drawScreen(MatrixStack matrices, double mouseX, double mouseY){}
    }
}

