package me.stormcph.lumina.module.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.Render3DEvent;
import me.stormcph.lumina.event.impl.RenderLabelEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;
import java.text.DecimalFormat;

public class Nametags extends Module {

    private final NumberSetting size = new NumberSetting("Size", 1, 10, 1, 1);
    private final BooleanSetting ping = new BooleanSetting("Show Ping", false);
    private final BooleanSetting fillBg = new BooleanSetting("Background", false);
    private final BooleanSetting health = new BooleanSetting("Health", false);

    public Nametags() {
        super("Nametags", "Show more infor on player's names", Category.RENDER);
        addSettings(size, ping, health, fillBg);
    }

    @EventTarget
    public void onNameTagRender(RenderLabelEvent e) {
        if(nullCheck()) return;
        if(e.getEntity() instanceof PlayerEntity) {
            e.setCancelled(true);
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent e) {
        for(PlayerEntity player : mc.world.getPlayers()) {
            if(player == mc.player) continue;
            drawNametag(player);
        }
    }

    private void drawNametag(PlayerEntity player) {
        double x = player.getX();
        double y = player.getY() + player.getHeight() + 0.5;
        double z = player.getZ();

        double offX = 0;
        double offY = 0;

        double scale = size.getValue();

        PlayerListEntry entry = mc.player.networkHandler.getPlayerListEntry(player.getUuid());

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String roundedHealth = decimalFormat.format(player.getHealth());

        String name = (player.getName().getString().equalsIgnoreCase("CorruptionHades") ? "§4" : "")
                + player.getDisplayName().getString()
                + (ping.isEnabled() ? " " + entry.getLatency() + "ms" : "")
                + (health.isEnabled() ? " §c" + roundedHealth + "❤§r" : "");
        Text text = Text.of(name);

        drawText(text, x, y, z, offX, offY, scale, fillBg.isEnabled());
    }

    private void drawText(Text text, double x, double y, double z, double offX, double offY, double scale, boolean fill) {
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
            mc.textRenderer.draw(text, -halfWidth, 0f, Color.white.getRGB(), false, matrices.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.SEE_THROUGH, new Color(255, 255, 255, 100).getRGB(), 1);
            immediate.draw();
        } else {
            matrices.push();
            matrices.translate(0, -1, 0);
            mc.textRenderer.draw(text, -halfWidth, 0f, Color.white.getRGB(), false, matrices.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.SEE_THROUGH, new Color(255, 255, 255, 0).getRGB(), 1);
            immediate.draw();
            matrices.pop();
        }

       // mc.textRenderer.draw(text, -halfWidth, 0f, -1, false, matrices.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.NORMAL, Color.white.getRGB(), 0);
      //  immediate.draw();

        RenderSystem.disableBlend();
    }

    private MatrixStack matrixFrom(double x, double y, double z) {
        MatrixStack matrices = new MatrixStack();

        Camera camera = mc.gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));

        matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

        return matrices;
    }
}
