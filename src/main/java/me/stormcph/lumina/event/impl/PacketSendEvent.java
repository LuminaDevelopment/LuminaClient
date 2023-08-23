package me.stormcph.lumina.event.impl;

import net.minecraft.network.packet.Packet;

public class PacketSendEvent extends PacketEvent {
    public PacketSendEvent(Packet<?> packet) {
        super(packet);
    }
}
