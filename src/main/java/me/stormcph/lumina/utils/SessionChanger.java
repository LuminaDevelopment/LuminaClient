package me.stormcph.lumina.utils;

import me.stormcph.lumina.mixins.ISession;
import net.minecraft.client.MinecraftClient;

public class SessionChanger {

    public static void changeSession(String username, String uuid, String token) {

    }

    public static void loginCracked(String username) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ((ISession) mc.getSession()).lumina$setUsername(username);
    }
}
