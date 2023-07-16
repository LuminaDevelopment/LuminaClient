package me.stormcph.lumina.utils.player;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;

public class PacketUtil {

    public static void sendPacket(Packet<?> packet) {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.getNetworkHandler().sendPacket(packet);
    }

    // TODO: Make method for eventless packet sending
}
