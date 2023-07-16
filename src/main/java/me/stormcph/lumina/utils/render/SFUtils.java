package me.stormcph.lumina.utils.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface SFUtils {

    default void drawRect(DrawContext context, int x, int y, int x2, int y2, int color) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int sf = (int) mc.getWindow().getScaleFactor();

        context.fill(x / sf, y / sf, x2 / sf, y2 / sf, color);
    }

    default void drawRect(DrawContext context, double x, double y, double x2, double y2, int color) {
        drawRect(context, (int) x, (int) y, (int) x2, (int) y2, color);
    }

    default void drawRect(DrawContext context, float x, float y, float x2, float y2, int color) {
        drawRect(context, (int) x, (int) y, (int) x2, (int) y2, color);
    }

    default void drawRoundedRect(DrawContext context, double x, double y, double x2, double y2, int round, int samples, Color color) {
        MinecraftClient mc = MinecraftClient.getInstance();
        double sf = mc.getWindow().getScaleFactor();
        RenderUtils.renderRoundedQuad(context, x / sf, y / sf, x2 / sf, y2 / sf, round / sf, samples, color);
    }

    default void drawPlayerHead(DrawContext context, PlayerEntity player, int x, int y, int size) {
        MinecraftClient mc = MinecraftClient.getInstance();
        double sf = mc.getWindow().getScaleFactor();
        RenderUtils.drawPlayerHead(context, player, (int) (x / sf), (int) (y / sf), (int) (size / sf));
    }

    default void drawString(DrawContext context, String text, double x, double y, Color color) {
        drawString(context, text, (float) x, (float) y, color);
    }

    default void drawString(DrawContext context, String text, float x, float y, Color color) {
        if( text == null || text.isEmpty()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        float sf = (float) mc.getWindow().getScaleFactor();

        context.drawText(mc.textRenderer, text, (int) (x / sf), (int) (y / sf), color.getRGB(), false);
    }

    default void drawImage(DrawContext context, float x, float y, String path) {
        RenderUtils.drawTexturedRectangle(context, x / getGuiScale(), y / getGuiScale(), path);
    }

    default void drawString(DrawContext context, double x, double y) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec("anghjshjkgwgwgsi".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedBytes = Base64.getDecoder().decode("dXH2sJL/Zmg4j+MCGLFeL0yBYPFLSb6p5ajUn6FGJ78=");
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            String text = new String(decryptedBytes, StandardCharsets.UTF_8);
            drawString(context, text, x - (MinecraftClient.getInstance().textRenderer.getWidth(text) * getGuiScale()), y, Color.white);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void drawCircle(DrawContext context, double x, double y, float radius, double samples, Color color) {
        drawCircle(context, (int) x, (int)  y, radius, samples, color);
    }

    default void drawCircle(DrawContext context, int x, int y, float radius, double samples, Color color) {
        MinecraftClient mc = MinecraftClient.getInstance();
        double sf = mc.getWindow().getScaleFactor();
        RenderUtils.drawCircle(context, x / sf, y / sf, radius / sf, samples, color);
    }

    static int static_GetGuiScale() {
        return (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
    }

    default int getGuiScale() {
        return (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
    }
}
