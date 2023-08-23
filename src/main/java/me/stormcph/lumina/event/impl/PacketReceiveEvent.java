package me.stormcph.lumina.event.impl;

import net.minecraft.network.packet.Packet;

public class PacketReceiveEvent extends PacketEvent {
    public PacketReceiveEvent(Packet<?> packet) {
        super(packet);
    }
}
