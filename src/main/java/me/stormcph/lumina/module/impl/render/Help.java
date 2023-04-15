package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class Help extends HudModule {
    private boolean firstRender = false;
    public Help() {
        super(
                "Watermark",
                "Client watermark",
                Category.RENDER,
                MinecraftClient.getInstance().getWindow().getScaledWidth()-30,
                MinecraftClient.getInstance().textRenderer.fontHeight*2+4,
                7,
                7
        );
        this.setEnabled(true);
    }
    @Override
    public void draw(MatrixStack matrices) {
        if(!firstRender){ // prevents it from being rendered in the middle of the screen and on the edge instead
            setX(MinecraftClient.getInstance().getWindow().getScaledWidth()-30);
            setY(MinecraftClient.getInstance().textRenderer.fontHeight*2+4);
        }

        // TODO: add multiple hud modules, such as fps, etc.
        // TODO: add modes like OnlyTitle, Help, etc.



        TextRenderer renderer = mc.textRenderer;
        Color titleColor = RenderUtils.getMcColor(161, 3, 252);
        renderer.drawWithShadow(matrices, "LuminaClient", getX()-renderer.getWidth("LuminaClient"), getY()-renderer.fontHeight*2-2, titleColor.getRGB());
        renderer.drawWithShadow(matrices, "To open ClickGUI, press RALT", getX()-renderer.getWidth("To open ClickGUI, press RALT"), getY()-renderer.fontHeight, -1);
    }
}
