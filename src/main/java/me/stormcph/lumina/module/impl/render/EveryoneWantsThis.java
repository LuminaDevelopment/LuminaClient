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

public class EveryoneWantsThis extends HudModule {

    private String[] frames;
    private int index = 0;
    private int counter = 0;

    private final BooleanSetting proof = new BooleanSetting("Proof", false);

    public EveryoneWantsThis() {
        super("EveryoneWantsThis", "Yes", Category.RENDER, 50, 50, 69, 69);
        addSettings(proof);
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

        setWidth(180);
        setHeight(245);

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
        RenderUtils.drawTexturedRectangle(matrices, getX() * (1 /scale), getY() * (1 /scale), "textures/yes/" + frames[index]);
        matrices.pop();
    }
}
