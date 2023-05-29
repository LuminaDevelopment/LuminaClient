package me.stormcph.lumina.utils.chat.message;


import me.stormcph.lumina.event.Event;
import me.stormcph.lumina.event.EventManager;
import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.ChatMessageReceivedEvent;
import me.stormcph.lumina.event.impl.ChatMessageSentEvent;
import me.stormcph.lumina.event.impl.OnWorldLoadEvent;
import me.stormcph.lumina.utils.chat.ChatUtils;
import net.minecraft.client.util.telemetry.WorldLoadedEvent;

import java.util.ArrayList;
import java.util.List;

public class ChatMessage {

    private List<String> lines;


    /**
     * @param lines All the message lines, as a basic string array
     */
    public ChatMessage(String... lines){
        this.lines = List.of(lines);
    }


    /**
     * @param lines A List<String> object with the message lines.
     */
    public ChatMessage(List<String> lines){
        this.lines = lines;
    }


    /**
     * Immediately attempts to send the message.
     */
    public void send(){
        for(String line : lines) ChatUtils.sendMsg(line);
    }


    /**
     * @param trigger The event to queue for (message will be removed from queue once sent)
     * Currently supports OnWorldLoadEvent, ChatMessageReceivedEvent, ChatMessageSentEvent.
     * @see OnWorldLoadEvent
     * @see ChatMessageReceivedEvent
     * @see ChatMessageSentEvent
     */
    public void queue(Event trigger){
        if(trigger instanceof OnWorldLoadEvent) onWorldLoadMessageQueue.add(this);
        else if(trigger instanceof ChatMessageReceivedEvent) ChatMessageReceivedMessageQueue.add(this);
        else if(trigger instanceof ChatMessageSentEvent) ChatMessageSentMessageQueue.add(this);
    }



    private static final List<ChatMessage> onWorldLoadMessageQueue = new ArrayList<>();
    private static final List<ChatMessage> ChatMessageReceivedMessageQueue = new ArrayList<>();
    private static final List<ChatMessage> ChatMessageSentMessageQueue = new ArrayList<>();

    /**
     * For the message queue
     */
    public static void init(){
        EventManager.register(ChatMessage.class);
    }

    @EventTarget
    public void onWorldLoad(WorldLoadedEvent e){
        for(ChatMessage message : onWorldLoadMessageQueue) message.send();
        onWorldLoadMessageQueue.clear();
    }

    @EventTarget
    public void onChatMessage2S(ChatMessageSentEvent e){
        for(ChatMessage message : ChatMessageSentMessageQueue) message.send();
        ChatMessageSentMessageQueue.clear();
    }

    @EventTarget
    public void onChatMessage2C(ChatMessageReceivedEvent e){
        for(ChatMessage message : ChatMessageReceivedMessageQueue) message.send();
        ChatMessageReceivedMessageQueue.clear();
    }

    // TODO: 29/05/2023 add support for more events

}