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
    public ChatMessage(boolean prefixed, String... lines){
        this.lines = List.of(lines);
    }


    /**
     * Adds a line to the message
     * @param line The line (string object)
     * @return the ChatMessage object
     */
    public ChatMessage addLine(String line){
        lines.add(line);
        return this;
    }


    /**
     * @param lines A List<String> object with the message lines.
     */
    public ChatMessage(boolean prefixed, List<String> lines){
        this.lines = lines;
    }


    /**
     * Immediately attempts to send the message.
     */
    public void send(boolean prefix){
        for(String line : lines) ChatUtils.sendMsg(line, prefix);
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
    public static void onWorldLoad(OnWorldLoadEvent e){
        for(ChatMessage message : onWorldLoadMessageQueue) message.send(false);
        onWorldLoadMessageQueue.clear();
    }

    @EventTarget
    public static void onChatMessage2S(ChatMessageSentEvent e){
        for(ChatMessage message : ChatMessageSentMessageQueue) message.send(false);
        ChatMessageSentMessageQueue.clear();
    }

    @EventTarget
    public static void onChatMessage2C(ChatMessageReceivedEvent e){
        for(ChatMessage message : ChatMessageReceivedMessageQueue) message.send(false);
        ChatMessageReceivedMessageQueue.clear();
    }

    // TODO: 29/05/2023 add support for more events

}