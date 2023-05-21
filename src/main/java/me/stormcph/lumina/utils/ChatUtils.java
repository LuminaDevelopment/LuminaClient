package me.stormcph.lumina.utils;

import me.stormcph.lumina.module.impl.misc.NoTrace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatUtils {

    public static void sendMsg(String message) {

        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc.player == null) return;
        if(NoTrace.shouldLog()) mc.player.sendMessage(Text.of(message.replace("&", "§")));
    }
}
