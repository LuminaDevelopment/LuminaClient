package me.stormcph.lumina.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class RenderUtils {

    // Stolen from DrawableHelper
    public static void fill(@NotNull MatrixStack matrices, double x1, double y1, double x2, double y2, int color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        double i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        float f = (float) (color >> 24 & 0xFF) / 255.0f;
        float g = (float) (color >> 16 & 0xFF) / 255.0f;
        float h = (float) (color >> 8 & 0xFF) / 255.0f;
        float j = (float) (color & 0xFF) / 255.0f;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix, (float) x1, (float) y2, 0.0f).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float) x2, (float) y2, 0.0f).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float) x2, (float) y1, 0.0f).color(g, h, j, f).next();
        bufferBuilder.vertex(matrix, (float) x1, (float) y1, 0.0f).color(g, h, j, f).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    public static void drawHollowRect(MatrixStack matrixStack, int x, int y, int width, int height, int color, int thickness) {
        fill(matrixStack, x, y - thickness, x - thickness, y + height + thickness, color);
        fill(matrixStack, x + width, y - thickness, x + width + thickness, y + height + thickness, color);

        fill(matrixStack, x, y, x + width, y - thickness, color);
        fill(matrixStack, x, y + height, x + width, y + height + thickness, color);
    }

    public static void scale(MatrixStack matrices, float x, float y, float scale) {
        matrices.translate(x, y, 0);
        matrices.scale(scale, scale, 1);
        matrices.translate(-x, -y, 0);
    }

    public static void drawScaledTexturedRect(MatrixStack matrices, float x, float y, float scale, String path) {
        // Scale
        matrices.push();
        matrices.scale(scale, scale, 0);
        // Bind the texture
        RenderSystem.setShaderTexture(0, new Identifier("lumina", path));
        try {
            URL url = RenderUtils.class.getResource("assets/lumina/" + path);

            if(url == null) return;
            // Get dimensions of image
            BufferedImage image = ImageIO.read(url);
            // Draw the image
            DrawableHelper.drawTexture(matrices, (int) (x / scale), (int) (y / scale), 0.0f, 0.0f, image.getWidth(), image.getHeight(), image.getWidth(), image.getHeight());
            matrices.pop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Draws an image at specified coords
     * @param matrices The gl context to draw with
     * @param x X pos
     * @param y y pos
     * @param path Path to the image
     */
    public static void drawTexturedRectangle(MatrixStack matrices, float x, float y, String path) {
        // Bind the texture
        RenderSystem.setShaderTexture(0, new Identifier("lumina", path));
        try {
            URL url = RenderUtils.class.getResource("/assets/lumina/" + path);
            BufferedImage image = ImageIO.read(url);
            DrawableHelper.drawTexture(matrices, (int) x, (int) y, 0.0f, 0.0f, image.getWidth(), image.getHeight(), image.getWidth(), image.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Color getMcColor(int r, int b, int g){
        return new Color(b, g, r);
    }
}
