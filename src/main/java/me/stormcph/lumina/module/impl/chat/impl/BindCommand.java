package me.stormcph.lumina.module.impl.chat.impl;

import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.chat.ChatCommand;
import me.stormcph.lumina.utils.player.ChatUtils;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class BindCommand extends ChatCommand {

    public BindCommand() {
        super("bind");
    }

    @Override
    public void execute(List<String> args) {
        if (args.size() == 2) {
            int key = Integer.parseInt(args.get(0));
            String module = args.get(1);
            Module module1 = ModuleManager.INSTANCE.getModuleByName(module);
            if(module1 != null) {
                module1.setKey(key);
                ChatUtils.sendMsg("&7Bound &a" + module1.getName() + "&7 to &a" + GLFW.glfwGetKeyName(key, 0));
            } else {
                ChatUtils.sendMsg("&cModule not found");
            }
        } else {
            ChatUtils.sendMsg("&cUsage: .bind <key> <module>");
        }
    }
}
