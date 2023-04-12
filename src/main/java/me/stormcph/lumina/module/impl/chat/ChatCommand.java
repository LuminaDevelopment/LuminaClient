package me.stormcph.lumina.module.impl.chat;

import java.util.List;

/**
 * The base class for a chat command.
 * All chat commands have to be registered in ChatCommandManager.java
 */
public class ChatCommand {

    /**
     * The command name storage.
     */
    public String name;

    /**
     * @param name The name used to detect the function (without prefix)
     */
    public ChatCommand(String name){
        this.name = name;
    }

    /**
     * The command that holds the code for the function.
     * @param args The command arguments
     */
    public void execute(List<String> args){}
}
