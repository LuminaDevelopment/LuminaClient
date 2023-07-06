package me.stormcph.lumina.module.impl.player;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.mixinterface.IPMC2SP;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Prevents fall damage.", Category.PLAYER);
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent e) {
        if (e.getPacket() instanceof PlayerMoveC2SPacket castPacket) {
            IPMC2SP newPacket = (IPMC2SP) castPacket;
            newPacket.setOnGround(true);
            e.setPacket((Packet<?>) newPacket);
        }
    }
}
