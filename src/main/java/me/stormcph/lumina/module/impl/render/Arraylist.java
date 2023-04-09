package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

public class Arraylist extends HudModule {

    public Arraylist() {
        super("Arraylist", "Show enabled mods", Category.RENDER, 20, 20, 10, 10);
    }

    @Override
    public void draw(MatrixStack matrices) {
        int index = 0;
        int sWidth = mc.getWindow().getScaledWidth();
        int sHeight = mc.getWindow().getScaledHeight();

        // TODO: coordinate usage & set width/height

        List<Module> enabled = ModuleManager.INSTANCE.getEnabledModules();

        enabled.sort((m1, m2) -> (int) (mc.textRenderer.getWidth(m2.getName() + m2.getDisplayName()) - mc.textRenderer.getWidth(m1.getName() + m1.getDisplayName())));

        for (Module mod : enabled) {
            mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), (sWidth - 4) - mc.textRenderer.getWidth(mod.getDisplayName()), 10 + (index * mc.textRenderer.fontHeight), -1);
            index++;
        }
    }
}
