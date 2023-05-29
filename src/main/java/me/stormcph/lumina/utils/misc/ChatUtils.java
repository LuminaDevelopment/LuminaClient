package me.stormcph.lumina.utils.misc;

import me.stormcph.lumina.module.impl.misc.NoTrace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatUtils {

    public static void sendMsg(String message) {
        String clientMessage = "&7[&d&lLumina &5&lClient&7] &f " + message;
        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc.player == null) return;
        if(NoTrace.shouldLog()) mc.player.sendMessage(Text.of(clientMessage.replace("&", "§")));
    }
}

    /* public static void sendPrefixMessage(String message) {
        String prefix = "&7[&d&lLumina &5&lClient&7] &f";
        sendMsg(prefix + message);
    }*/ //this didn't work for me

