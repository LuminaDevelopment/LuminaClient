package me.stormcph.lumina.event.impl;

import me.stormcph.lumina.event.Event;

public class ChatMessageSentEvent extends Event {

    private String message;

    public ChatMessageSentEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
