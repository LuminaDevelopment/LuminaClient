package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class EveryoneWantsThis extends HudModule {

    private String[] frames;
    private int index = 0;
    private int counter = 0;

    private NumberSetting scaleSetting;

    private final BooleanSetting proof = new BooleanSetting("Proof", false);

    public EveryoneWantsThis() {
        super("EveryoneWantsThis", "Yes", Category.RENDER, 50, 50, 69, 69);
        scaleSetting = new NumberSetting("Scale", 0.08, 2.0, 0.35, 0.001);
        addSettings(proof, scaleSetting);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        frames = new String[]{"1.png", "2.png", "3.png", "4.png", "5.png"};

        counter = 0;
        index = 0;

        if(proof.isEnabled()) {
            mc.setScreen(new Screen(Text.of("Proof")) {
                @Override
                public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                    super.render(matrices, mouseX, mouseY, delta);
                    float scale = 0.65f;
                    matrices.push();
                    matrices.scale(scale, scale, 0);
                    RenderUtils.drawTexturedRectangle(matrices, 10, 20, "textures/yes/img.png");
                    matrices.pop();
                    mc.textRenderer.draw(matrices, "Proof (ur welcome True Kangz)", 10, 3, -1);
                }
            });
        }
    }

    @Override
    public void draw(MatrixStack matrices) {
        if (frames == null) return;

        float scale = (float) scaleSetting.getValue();
        int width = (int) (scale * 512);
        int height = (int) (scale * 703);

        setWidth(width);
        setHeight(height);

        counter++;
        if(counter >= 8) {
            if(index >= frames.length - 1) {
                index = 0;
            } else {
                index++;
            }

            counter = 0;
        }

        matrices.push();
        matrices.scale(scale, scale, 0);
        RenderUtils.drawTexturedRectangle(matrices, getX() * (1 /scale), getY() * (1 /scale), "textures/yes/" + frames[index]);
        matrices.pop();
    }
}
