package me.stormcph.lumina.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
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

    // Credit to Coffee client: https://github.com/business-goose/Coffee/tree/master
    public static void renderRoundedQuad(MatrixStack matrices, double fromX, double fromY, double toX, double toY, double rad, double samples, Color c) {
        int color = c.getRGB();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = (float) (color >> 24 & 255) / 255.0F;
        float g = (float) (color >> 16 & 255) / 255.0F;
        float h = (float) (color >> 8 & 255) / 255.0F;
        float k = (float) (color & 255) / 255.0F;
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad, samples);

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        double toX1 = toX - rad;
        double toY1 = toY - rad;
        double fromX1 = fromX + rad;
        double fromY1 = fromY + rad;
        double[][] map = new double[][]{new double[]{toX1, toY1}, new double[]{toX1, fromY1}, new double[]{fromX1, fromY1}, new double[]{fromX1, toY1}};
        for (int i = 0; i < 4; i++) {
            double[] current = map[i];
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
            }
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawPlayerHead(MatrixStack matrices, PlayerEntity player, float x, float y, int size) {
        MinecraftClient mc = MinecraftClient.getInstance();

        GameProfile gameProfile = new GameProfile(player.getUuid(), player.getName().getString());
        PlayerListEntry playerListEntry = mc.player.networkHandler.getPlayerListEntry(gameProfile.getId());

        boolean bl22 = player != null && LivingEntityRenderer.shouldFlipUpsideDown(player);
        boolean bl3 = player != null && player.isPartVisible(PlayerModelPart.HAT);
        RenderSystem.setShaderTexture(0, playerListEntry.getSkinTexture());
        PlayerSkinDrawer.draw(matrices, (int) x, (int) y, 15, bl3, bl22);
    }

    /**
     * Wrapper method
     */
    public static void drawCircle(MatrixStack matrices, double centerX, double centerY, double radius, double samples, Color color) {
        drawCircle(matrices, centerX, centerY, radius, samples, color.getRGB());
    }

    /**
     * Draws a circle
     * @param matrices ...
     * @param centerX CenterX of the Circle
     * @param centerY CenterY of the circle
     * @param radius "Bigness" of the circle
     * @param samples How many pixels should be used
     * @param color Color
     */
    public static void drawCircle(MatrixStack matrices, double centerX, double centerY, double radius, double samples, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        RenderSystem.enableBlend();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        for (double r = 0; r < 360; r += (360 / samples)) {
            float rad1 = (float) Math.toRadians(r);
            float sin = (float) (Math.sin(rad1) * radius);
            float cos = (float) (Math.cos(rad1) * radius);
            bufferBuilder.vertex(matrix, (float) centerX + sin, (float) centerY + cos, 0.0F).color(red, green, blue, alpha).next();
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
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
