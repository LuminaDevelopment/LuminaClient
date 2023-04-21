package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.utils.GifTool;
import me.stormcph.lumina.utils.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LuminaLogo extends HudModule {

    private String[] frames;
    private int index = 0;
    private int counter = 0;

    public LuminaLogo() {
        super("LuminaLogo", "The Best Module", Category.RENDER, 16, 16, 16, 16);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        frames = new String[]{"logo.png"};

        counter = 0;
        index = 0;

    }

    @Override
    public void draw(MatrixStack matrices) {
        if (frames == null) return;

        setWidth(16);
        setHeight(16);

        counter++;
        if(counter >= 8) {
            if(index >= frames.length - 1) {
                index = 0;
            } else {
                index++;
            }

            counter = 0;
        }

        float scale = 0.35f;
        matrices.push();
        matrices.scale(scale, scale, 0);
        RenderUtils.drawTexturedRectangle(matrices, getX() * (1 /scale), getY() * (1 /scale), "textures/gui/" + frames[index]);
        matrices.pop();
    }
}
