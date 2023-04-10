package me.stormcph.lumina.module.impl.misc;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PacketLogger extends Module {
    public PacketLogger() {
        super("PacketLogger", "Show sent packets", Category.MISC);
    }

    @EventTarget
    public void onPacket(PacketSendEvent e) {
      //  if(e.getPacket() instanceof PlayerMoveC2SPacket.Full pmp) {
            sendMsg("Sent packet: " + e.getPacket().getClass().getSimpleName());
      //  }
    }
}
