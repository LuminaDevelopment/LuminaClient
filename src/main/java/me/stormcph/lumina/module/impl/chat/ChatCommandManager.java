package me.stormcph.lumina.module.impl.chat;

import me.stormcph.lumina.module.impl.chat.impl.HelpCommand;
import me.stormcph.lumina.utils.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatCommandManager {

    public static String cachedName;

    private static final List<ChatCommand> commandList = new ArrayList<>();

    public static void addCommand(ChatCommand c){
        commandList.add(c);
    }

    public static void init(){ // Add commands here
        addCommand(new HelpCommand());


        cachedName = "null";
    }

    public static ChatCommand findCommand(String name){
        for(ChatCommand c : commandList){
            if(c.name.equals(name)) return c;
        }
        cachedName = name;

        return new ChatCommand("invalid"){
            @Override
            public void execute(List<String> args) {
                ChatUtils.sendMsg("Invalid command!");
            }
        };
    }




}
