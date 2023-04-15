package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class Help extends HudModule {
    public Help() {
        super(
                "ClientHelp",
                "Helps new users understand how to use",
                Category.RENDER,
                MinecraftClient.getInstance().getWindow().getScaledWidth()-30,
                MinecraftClient.getInstance().getWindow().getScaledHeight()-30,
                28,
                28
        );
        this.setEnabled(true);
    }

    @Override
    public void draw(MatrixStack matrices) {
        TextRenderer renderer = mc.textRenderer;
        renderer.drawWithShadow(matrices, "To open ClickGUI, press RALT!", getX()-renderer.getWidth(""), getY()-renderer.fontHeight, -1);
    }
}
