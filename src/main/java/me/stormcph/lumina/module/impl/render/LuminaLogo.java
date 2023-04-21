package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LuminaLogo extends HudModule {

    private String[] frames;
    private int index = 0;
    private int counter = 0;

    private NumberSetting scaleSetting;

    public LuminaLogo() {
        super("LuminaLogo", "The Best Module", Category.RENDER, 256, 256, 256, 256);
        scaleSetting = new NumberSetting("Scale", 0.08, 2.0, 0.35, 0.001);
        this.addSettings(scaleSetting);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        frames = new String[]{"logo_256x.png"};

        counter = 0;
        index = 0;

    }

    @Override
    public void draw(MatrixStack matrices) {
        if (frames == null) return;

        float scale = (float) scaleSetting.getValue();
        int width = (int) (scale * 256);
        int height = (int) (scale * 256);

        setWidth(width);
        setHeight(height);

        counter++;

        matrices.push();
        matrices.scale(scale, scale, 0);
        RenderUtils.drawTexturedRectangle(matrices, getX() * (1 / scale), getY() * (1 / scale), "textures/gui/" + frames[index]);
        matrices.pop();
    }
}
