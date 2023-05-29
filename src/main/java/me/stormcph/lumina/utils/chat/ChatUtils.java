package me.stormcph.lumina.utils.chat;

import me.stormcph.lumina.module.impl.misc.NoTrace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatUtils {

    public static void sendMsg(String message, boolean prefix) {
        String clientMessage = prefix ? "&7[&d&lLumina &5&lClient&7] &f " + message : message;
        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc.player == null) return;
        if(NoTrace.shouldLog()) mc.player.sendMessage(Text.of(clientMessage.replace("&", "§")));
    }

}