package me.stormcph.lumina.module.impl.misc;

import me.stormcph.lumina.event.EventManager;
import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PacketReceiveEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

public class AutoEz extends Module {

    private Entity lastHitEntity = null;
    BooleanSetting ezPop = new BooleanSetting("EzPop", false);
    BooleanSetting ggEz = new BooleanSetting("ggez", true);

    public AutoEz() {
        super("AutoEz", "says ez after kill", Category.MISC);
        addSettings(ggEz, ezPop);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        EventManager.register(this);
        sendMsg("Enabled");
    }


    @EventTarget
    public void onUpdate(PacketReceiveEvent event) {
        if (!(event.getPacket() instanceof EntityStatusS2CPacket))
            return;

        EntityStatusS2CPacket packet = (EntityStatusS2CPacket) event.getPacket();

        if (mc.world == null)
            return;

        Entity entity = packet.getEntity(mc.world);
        if (!(entity instanceof ZombieEntity))  // changed from PlayerEntity to ZombieEntity
            return;

        ZombieEntity zombie = (ZombieEntity) entity;  // changed from PlayerEntity to ZombieEntity

        if (packet.getStatus() == 35) {
            if (ezPop.isEnabled()) {
                sendMsg("ezpop" + " " + zombie.getEntityName());  // changed from player to zombie
            }
        }

        if (packet.getStatus() == 3) {
            if (ggEz.isEnabled()) {
                sendMsg("ggez" + " " + zombie.getEntityName()); // same here
            }
        }
    }
}
