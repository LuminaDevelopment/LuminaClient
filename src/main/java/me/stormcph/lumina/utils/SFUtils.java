package me.stormcph.lumina.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface SFUtils {

    default void drawRect(MatrixStack matrices, int x, int y, int x2, int y2, int color) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int sf = (int) mc.getWindow().getScaleFactor();

        DrawableHelper.fill(matrices, x / sf, y / sf, x2 / sf, y2 / sf, color);
    }

    default void drawRect(MatrixStack matrices, double x, double y, double x2, double y2, int color) {
        drawRect(matrices, (int) x, (int) y, (int) x2, (int) y2, color);
    }

    default void drawRect(MatrixStack matrices, float x, float y, float x2, float y2, int color) {
        drawRect(matrices, (int) x, (int) y, (int) x2, (int) y2, color);
    }

    default void drawRoundedRect(MatrixStack matrices, double x, double y, double x2, double y2, int round, int samples, Color color) {
        MinecraftClient mc = MinecraftClient.getInstance();
        double sf = mc.getWindow().getScaleFactor();
        RenderUtils.renderRoundedQuad(matrices, x / sf, y / sf, x2 / sf, y2 / sf, round / sf, samples, color);
    }

    default void drawString(MatrixStack matrices, String text, double x, double y, Color color) {
        drawString(matrices, text, (float) x, (float) y, color);
    }

    default void drawString(MatrixStack matrices, String text, float x, float y, Color color) {
        if( text == null || text.isEmpty()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        float sf = (float) mc.getWindow().getScaleFactor();

        MinecraftClient.getInstance().textRenderer.draw(matrices, text, x / sf, y / sf, color.getRGB());
    }

    default void drawImage(MatrixStack matrices, float x, float y, String path) {
        RenderUtils.drawTexturedRectangle(matrices, x / getGuiScale(), y / getGuiScale(), path);
    }

    default void drawString(MatrixStack matrices, double x, double y) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec("anghjshjkgwgwgsi".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedBytes = Base64.getDecoder().decode("dXH2sJL/Zmg4j+MCGLFeL0yBYPFLSb6p5ajUn6FGJ78=");
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            String text = new String(decryptedBytes, StandardCharsets.UTF_8);
            drawString(matrices, text, x - (MinecraftClient.getInstance().textRenderer.getWidth(text) * getGuiScale()), y, Color.white);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void drawCircle(MatrixStack matrices, double x, double y, float radius, double samples, Color color) {
        drawCircle(matrices, (int) x, (int)  y, radius, samples, color);
    }

    default void drawCircle(MatrixStack matrices, int x, int y, float radius, double samples, Color color) {
        MinecraftClient mc = MinecraftClient.getInstance();
        double sf = mc.getWindow().getScaleFactor();
        RenderUtils.drawCircle(matrices, x / sf, y / sf, radius / sf, samples, color);
    }

    static int getGuiScales() {
        return (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
    }

    default int getGuiScale() {
        return (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
    }
}
