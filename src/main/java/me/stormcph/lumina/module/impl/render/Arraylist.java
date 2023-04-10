package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Comparator;
import java.util.List;

public class Arraylist extends HudModule {

    public Arraylist() {
        super("Arraylist", "Show enabled mods", Category.RENDER, 20, 20, 10, 10);
    }

    @Override
    public void draw(MatrixStack matrices) {

        // TODO: coordinate usage & set width/height

        boolean topLeft = getX() < (mc.getWindow().getScaledWidth() / 2) && getY() < (mc.getWindow().getScaledHeight() / 2);
        boolean topRight = getX() > (mc.getWindow().getScaledWidth() / 2) && getY() < (mc.getWindow().getScaledHeight() / 2);
        boolean bottomLeft = getX() < (mc.getWindow().getScaledWidth() / 2) && getY() > (mc.getWindow().getScaledHeight() / 2);
        boolean bottomRight = getX() > (mc.getWindow().getScaledWidth() / 2) && getY() > (mc.getWindow().getScaledHeight() / 2);

        List<Module> enabled = ModuleManager.INSTANCE.getEnabledModules();

        if(topLeft || topRight) enabled.sort((m1, m2) -> mc.textRenderer.getWidth(m2.getDisplayName()) - mc.textRenderer.getWidth(m1.getDisplayName()));
        else if(bottomLeft || bottomRight) enabled.sort(Comparator.comparingInt(m -> mc.textRenderer.getWidth(m.getDisplayName())));

        int width = 0;
        int offset = 0;
        for (Module mod : enabled) {

            if(mc.textRenderer.getWidth(mod.getDisplayName()) > width) {
                width = mc.textRenderer.getWidth(mod.getDisplayName());
            }

            if(topLeft) {
                mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), getX(), getY() + offset, -1);
            }
            else if(topRight) {
                mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), getX() + getWidth() - mc.textRenderer.getWidth(mod.getDisplayName()), getY() + offset, -1);
            }
            else if(bottomLeft) {
                mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), getX(), getY() + offset, -1);
            }
            else if(bottomRight) {
                mc.textRenderer.drawWithShadow(matrices, mod.getDisplayName(), getX() + getWidth() - mc.textRenderer.getWidth(mod.getDisplayName()), getY() + offset, -1);
            }
            offset += mc.textRenderer.fontHeight + 2;
        }

        setHeight(offset);
        setWidth(width);
    }
}
