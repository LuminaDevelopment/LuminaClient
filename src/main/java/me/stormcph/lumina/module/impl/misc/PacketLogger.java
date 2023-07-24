package me.stormcph.lumina.module.impl.misc;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.event.impl.PacketReceiveEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import net.minecraft.network.packet.Packet;

public class PacketLogger extends Module {

    private final BooleanSetting packetSend = new BooleanSetting("PacketSend", true);
    private final BooleanSetting packetReceive = new BooleanSetting("PacketReceive", false);

    public PacketLogger() {
        super("PacketLogger", "Show sent and received packets", Category.MISC);
        this.addSettings(packetSend, packetReceive);
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent e) {
        if (packetSend.isEnabled()) {
            sendMsg("Sent packet: " + e.getPacket().getClass().getSimpleName());
        }
    }

    @EventTarget
    public void onPacketReceive(PacketReceiveEvent e) {
        if (packetReceive.isEnabled()) {
            sendMsg("Received packet: " + e.getPacket().getClass().getSimpleName());
        }
    }
}
