package me.stormcph.lumina.module.impl.chat;

import me.stormcph.lumina.module.impl.chat.impl.*;
import me.stormcph.lumina.utils.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatCommandManager {

    public static String cachedName;

    private static final List<ChatCommand> commandList = new ArrayList<>();
    private static final ChatCommand INVALID_COMMAND = new ChatCommand("invalid") {
        @Override
        public void execute(List<String> args) {
            ChatUtils.sendMsg("&4Invalid command!");
        }
    };

    /**
     * Registers a chat command
     * @param c The command object
     */
    public static void addCommand(ChatCommand c){
        commandList.add(c);
    }

    /**
     * The initializer of this class.
     * Optimally, this only has to be run only once.
     */
    public static void init(){ // Add commands here
        addCommand(new HelpCommand());
        addCommand(new BindCommand());


        cachedName = "null";
    }

    /**
     *
     * @param command The command name to search for.
     * @return Returns the found command. If a command isn't found with the given arguments, an "invalid command" message is returned.
     */
    public static ChatCommand findCommand(String command){
        for(ChatCommand c : commandList){
            if(command.equalsIgnoreCase(c.name)) return c;
        }
        cachedName = command;

        return INVALID_COMMAND;
    }
}
