package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.old_ui.HudConfigScreen;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.Animation;
import me.stormcph.lumina.utils.SFUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;

public class TargetHUD extends HudModule implements SFUtils {
    
    private final NumberSetting range = new NumberSetting("Range", 1, 6, 4, 0.1);

    private final Animation popAnim = new Animation(0, 0);
    private final Animation healthBarAnim = new Animation(0, 0);

    public TargetHUD() {
        super("TargetHUD", "Renders the nearest player", Category.RENDER, 20, 20, 50, 30);
        addSettings(range);
    }

    @Override
    public void draw(MatrixStack matrices) {

        int sf = getGuiScale();

        PlayerEntity target = nearestPlayer();

        // TODO: add scale animation
        popAnim.update();

        if (target == null) {
            popAnim.setEnd(0);

            if (mc.currentScreen instanceof HudConfigScreen) {
                mc.textRenderer.draw(matrices, "TargetHUD", getX() + 10, getY() + 10, -1);
            }
            return;
        }
        else popAnim.setEnd(1);

        double percentage = (target.getHealth() / target.getMaxHealth()) * 100.0;

        setWidth(100);
        setHeight(40);

        drawRoundedRect(matrices, getX() * sf, getY() * sf, (getX() + getWidth()) * sf, (getY() + getHeight()) * sf, 5, 20, new Color(35, 35, 35));
        drawPlayerHead(matrices, target, (getX() + 5) * sf, (getY() + 7) * sf, 19);

        healthBarAnim.update();

        float endX = (float) (45 * (percentage / 100));
        healthBarAnim.setEnd(endX);
        int x = getX() + 25;
        drawRect(matrices, x * sf, (getY() + 18) * sf, (int) (x + healthBarAnim.getValue()) * sf, (getY() + 28) * sf, -1);

        mc.textRenderer.draw(matrices, target.getName().getString(), (getX() + 25), (getY() + 5), -1);
        //drawString(matrices, target.getName().getString(), (getX() + 25) * sf, (getY() + 5) * sf, Color.WHITE, FontRenderers.font_normal);
        String healthPercentage = String.format("%.2f%%", percentage);
        //drawString(matrices, healthPercentage, (getX() + 28) * sf, (getY() + 16) * sf, Color.black , FontRenderers.font_normal);
        mc.textRenderer.draw(matrices, healthPercentage, (getX() + 27) , (getY() + 19) , Color.black.getRGB());
    }

    private PlayerEntity nearestPlayer() {
        for(PlayerEntity entity : mc.world.getPlayers()) {
            if(mc.player.distanceTo(entity) <= range.getFloatValue()) {
                if(entity != mc.player) return entity;
            }
        }

        return null;
    }
}
