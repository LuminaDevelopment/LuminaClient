package me.stormcph.lumina.module.impl.chat.impl;

import me.stormcph.lumina.module.impl.chat.ChatCommand;
import me.stormcph.lumina.utils.ChatUtils;

import java.util.List;

public class HelpCommand extends ChatCommand {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(List<String> args) {
        if(args.size()==1){

            new ChatUtils.Message("Help utility",
                    "To open ClickGUI, press RALT or RCTRL",
                    "To view all commands, type "+ ChatUtils.Message.annotate(".help <coming soon>"),
                    "To view all modules in chat (and what they do), type "+ ChatUtils.Message.annotate(".help <coming soon>")
            ).send();

        } else {
            ChatUtils.sendMsg("&bAn &4error&b occurred! &6(&cunknown argument size&6)");
        }
    }
}
