package me.stormcph.lumina.module.impl.misc;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.utils.TimerUtil;

import java.util.ArrayList;
import java.util.List;

public class Advertise extends Module {

    private TimerUtil timer;
    private final List<String> messages = new ArrayList<>();

    public Advertise() {
        super("Advertise", "", Category.MISC);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        timer = new TimerUtil();
        messages.clear();
        messages.add("Get Lumina Client at luminaclient com");
        messages.add("Bad at the game? Try Lumina Client (luminaclient com)");
        messages.add("Lumina Client is the best client for Minecraft (luminaclient com)");
        messages.add("Lumina Client on Top! (luminaclient com)");
        messages.add("You should get Lumina client at luminaclient com!");
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(nullCheck()) return;

        if(timer.hasReached(3000)) {
            mc.player.networkHandler.sendChatMessage(randomMessage());
            timer.reset();
        }
    }

    private String randomMessage() {
        return messages.get((int) (Math.random() * messages.size()));
    }
}
