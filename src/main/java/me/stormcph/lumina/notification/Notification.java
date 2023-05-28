package me.stormcph.lumina.notification;

import me.stormcph.lumina.utils.Animation;
import me.stormcph.lumina.utils.SFUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;

public class Notification implements SFUtils {

    private final String title, description;
    private final Animation animation;

    public Notification(String title, String description) {
        this.title = title;
        this.description = description;

        int titleWidth = MinecraftClient.getInstance().textRenderer.getWidth(Text.of(title));
        int descriptionWidth = MinecraftClient.getInstance().textRenderer.getWidth(Text.of(description));

        if(descriptionWidth > titleWidth) {
            this.animation = new Animation(0, 20 + (descriptionWidth * getGuiScale()));
        }
        else {
            this.animation = new Animation(0, 20 + (titleWidth * getGuiScale()));
        }
    }

    public void render(MatrixStack matrices, float y) {
        Window sr = MinecraftClient.getInstance().getWindow();
        float x = sr.getScaledWidth() * getGuiScale();

        animation.update(false, 10);

        if(animation.hasEnded()) {
            animation.setEnd(0);
        }

        if(animation.getValue() == 0.0 && animation.getEnd() == 0.0) {
            NotificationManager.INSTANCE.removeNotification(this);
        }

        drawRect(matrices, x - animation.getValue(), y, x, y + 70, Color.darkGray.getRGB());
        drawString(matrices, title, x - animation.getValue() + 10, y + 10, Color.white);
        drawString(matrices, description, x - animation.getValue() + 10, y + 40, Color.white);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
