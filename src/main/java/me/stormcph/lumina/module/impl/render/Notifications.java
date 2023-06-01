package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.Render2DEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.notification.NotificationManager;

public class Notifications extends Module {

    public Notifications() {
        super("Notifications", "Show info on screen", Category.RENDER);
        setEnabled(true);
    }

    @EventTarget
    public void onRender(Render2DEvent e) {
        NotificationManager.INSTANCE.renderNotifications(e.getMatrices());
    }
}
