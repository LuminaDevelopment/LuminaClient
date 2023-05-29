package me.stormcph.lumina.module.impl.combat;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.utils.time.TimerUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

public class Criticals extends Module {

    private Packet<?> cached;
    private TimerUtil timer;

    public Criticals() {
        super("Criticals", "// TODO ", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        timer = new TimerUtil();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

    }

    @EventTarget
    public void onPacket(PacketSendEvent e) {
        if(nullCheck()) return;
        Packet<?> packet = e.getPacket();
        if(packet instanceof PlayerInteractEntityC2SPacket pie) {

        }
    }
}
