package me.stormcph.lumina.module.impl;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PacketReceiveEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerActionResponseS2CPacket;

public class Test extends Module {

    public Test() {
        super("Test", "Developer module", Category.MISC);
    }

    private int count = 0;

    @Override
    public void onEnable() {
        super.onEnable();
        count = 0;
    }

    @EventTarget
    public void onPacketReceive(PacketReceiveEvent e) {
        if(e.getPacket() instanceof BlockUpdateS2CPacket) {
            System.out.println("Received block update packet: " + count);

                e.setCancelled(true);

        }
    }
}
