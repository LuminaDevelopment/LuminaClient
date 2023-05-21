package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class LuminaLogo extends HudModule {

    private String[] frames;
    private int index = 0;

    private final NumberSetting scaleSetting;

    public LuminaLogo() {
        super("LuminaLogo", "The Best Module", Category.RENDER, 4, 4, 256, 256);
        scaleSetting = new NumberSetting("Scale", 0.08, 2.0, 0.16, 0.001);
        this.addSettings(scaleSetting);
        this.setEnabled(true);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        frames = new String[]{"logo_256x.png"};

        index = 0;

    }

    @Override
    public void draw(MatrixStack matrices) {
        if(nullCheck()) return;
        if (frames == null) return;

        float scale = (float) scaleSetting.getValue();
        int width = (int) (scale * 256);
        int height = (int) (scale * 256);

        setWidth(width);
        setHeight(height);


        matrices.push();
        matrices.scale(scale, scale, 0);
        RenderUtils.drawTexturedRectangle(matrices, getX() * (1 / scale), getY() * (1 / scale), "textures/gui/" + frames[index]);
        matrices.pop();
    }
}
