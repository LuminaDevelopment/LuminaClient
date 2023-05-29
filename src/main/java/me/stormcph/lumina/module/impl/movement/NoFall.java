package me.stormcph.lumina.module.impl.movement;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.mixinterface.IPMC2SP;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", "Prevents fall damage.", Category.MOVEMENT);
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent e) {
        if (ModuleManager.INSTANCE.getModuleByName("NoFall").isEnabled() && e.getPacket() instanceof PlayerMoveC2SPacket castPacket) {
                IPMC2SP newPacket = (IPMC2SP) castPacket;
                newPacket.setOnGround(true);
                e.setPacket((Packet) newPacket);
        }
    }
}
