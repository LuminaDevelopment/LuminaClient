package me.stormcph.lumina.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;

public class WorldRenderUtil {

    private final static MinecraftClient mc = MinecraftClient.getInstance();

    public static void drawText(Text text, double x, double y, double z, double offX, double offY, double scale, boolean fill, Color textColor, Color bgColor) {
        MatrixStack matrices = matrixFrom(x, y, z);

        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        matrices.translate(offX, offY, 0);
        matrices.scale(-0.025f * (float) scale, -0.025f * (float) scale, 1);

        int halfWidth = mc.textRenderer.getWidth(text) / 2;
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

        if (fill) {
            matrices.translate(0, -1, 0);
            mc.textRenderer.draw(text, -halfWidth, 0f, textColor.getRGB(), false, matrices.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.SEE_THROUGH, new Color(0, 0,0, 0).getRGB(), 1);
            immediate.draw();
        } else {
            matrices.push();
            matrices.translate(0, -1, 0);
            mc.textRenderer.draw(text, -halfWidth, 0f, textColor.getRGB(), false, matrices.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.SEE_THROUGH, bgColor.getRGB(), 1);
            immediate.draw();
            matrices.pop();
        }

        // mc.textRenderer.draw(text, -halfWidth, 0f, -1, false, matrices.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.NORMAL, Color.white.getRGB(), 0);
        //  immediate.draw();

        RenderSystem.disableBlend();
    }

    public static MatrixStack matrixFrom(double x, double y, double z) {
        MatrixStack matrices = new MatrixStack();

        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));

        matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

        return matrices;
    }
}
