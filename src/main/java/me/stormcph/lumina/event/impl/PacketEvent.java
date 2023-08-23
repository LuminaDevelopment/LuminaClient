package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.network.packet.Packet;

public abstract class PacketEvent extends Event {

    private Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}
