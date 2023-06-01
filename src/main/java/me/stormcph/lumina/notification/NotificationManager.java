package me.stormcph.lumina.notification;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class NotificationManager {

    public static final NotificationManager INSTANCE = new NotificationManager();
    private final List<Notification> notifications = new ArrayList<>();

    public void registerNotification(String title, String description) {
        this.notifications.add(new Notification(title, description));
    }

    public void removeNotification(Notification notification) {
        this.notifications.remove(notification);
    }

    public void renderNotifications(MatrixStack matrices) {
        Window sr = MinecraftClient.getInstance().getWindow();
        float y = (float) (sr.getScaledHeight() * sr.getScaleFactor() - 80);
        try {
            for (Notification notification : notifications) {
                notification.render(matrices, y);
                y -= 80;
            }
        }
        catch (ConcurrentModificationException ignored) {}
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
