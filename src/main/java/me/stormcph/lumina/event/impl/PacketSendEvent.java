package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.network.Packet;

public class PacketSendEvent extends Event {

    private Packet<?> packet;

    public PacketSendEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}
