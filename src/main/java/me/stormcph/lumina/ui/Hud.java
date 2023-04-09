package me.stormcph.lumina.ui;

import me.stormcph.lumina.module.Mod;
import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Comparator;
import java.util.List;

public class Hud {

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public static void render(MatrixStack matrices, float tickDelta) {
        renderArrayList(matrices);
        //mc.textRenderer.drawWithShadow(matrices, "test", 10, 10, -1); Color.BLUE.getRGB();
    }

    public static void renderArrayList(MatrixStack matrices) {
        int index = 0;
        int sWidth = mc.getWindow().getScaledWidth();
        int sHeight = mc.getWindow().getScaledHeight();

        List<Mod> enabled = ModuleManager.ISTANCE.getEnabledModules();

        enabled.sort(Comparator.comparingInt(m -> (int)mc.textRenderer.getWidth(((Mod)m).getDisplayName())).reversed());

        for (Mod mod : enabled) {
            mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), (sWidth - 4) - mc.textRenderer.getWidth(mod.getDisplayName()), 10 + (index * mc.textRenderer.fontHeight), -1);
            index++;
        }
    }
}
