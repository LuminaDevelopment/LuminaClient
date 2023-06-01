package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class BetterLoadScreen extends HudModule {

    private List<String> frames;
    private int index = 0;
    private int counter = 0;

    public BetterLoadScreen() {
        super("BetterLoadScreen", "Yes", Category.RENDER, 50, 50, 69, 69);
    }

    @SuppressWarnings("uncheked")
    @Override
    public void onEnable() {
        super.onEnable();
        frames = new ArrayList<>();

        for(int i = 1; i<21; i++){
            frames.add("frame ("+i+")");
        }

        counter = 0;
        index = 0;
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (frames == null) return;

        setWidth(180);
        setHeight(245);

        counter++;
        if(counter >= 8) {
            if(index >= frames.size() - 1) {
                index = 0;
            } else {
                index++;
            }

            counter = 0;
        }

        float scale = 0.35f;
        matrices.push();
        matrices.scale(scale, scale, 0);
        RenderUtils.drawTexturedRectangle(matrices, getX() * (1 /scale), getY() * (1 /scale), "textures/LoadScreen/" + frames.get(index));
        matrices.pop();
    }
}
