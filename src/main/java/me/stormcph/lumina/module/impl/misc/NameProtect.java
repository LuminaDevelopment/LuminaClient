package me.stormcph.lumina.module.impl.misc;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.ChatMessageReceivedEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.TextSetting;

public class NameProtect extends Module {

    private final TextSetting name = new TextSetting("Name", "LuminaUser");

    public NameProtect() {
        super("NameProtect", "Hide your name", Category.MISC);
        addSettings(name);
    }

    @EventTarget
    public void onChat(ChatMessageReceivedEvent e) {
        if(nullCheck()) return;
        String message = e.getMessage().getString();
        if(message.contains(mc.getSession().getUsername())) {
            String newText = message.replace(mc.getSession().getUsername(), name.getText());
            sendMsg(newText);
            e.cancel();
        }
    }
}
