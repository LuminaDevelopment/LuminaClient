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

            ChatUtils.sendMsg("&dLuminaClient &bPrototype");
            ChatUtils.sendMsg("&5Help utility");
            ChatUtils.sendMsg("&aTo open ClickGUI, press RALT or RCTRL");
            ChatUtils.sendMsg("&aTo open HUDEditor, press H");

        } else {
            ChatUtils.sendMsg("&bAn &4error&b occurred! &6(&cunknown argument size&6)");
        }
    }
}
