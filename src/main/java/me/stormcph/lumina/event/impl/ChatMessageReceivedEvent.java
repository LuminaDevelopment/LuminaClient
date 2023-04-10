package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;
import net.minecraft.text.Text;

// TODO: hook this event
public class ChatMessageReceivedEvent extends Event {

    private Text message;

    public ChatMessageReceivedEvent(Text message) {
        this.message = message;
    }

    public Text getMessage() {
        return message;
    }

    public void setMessage(Text message) {
        this.message = message;
    }
}
