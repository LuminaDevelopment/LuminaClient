package me.stormcph.lumina.module.impl.chat.impl;

import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.chat.ChatCommand;
import me.stormcph.lumina.utils.chat.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class BindCommand extends ChatCommand {

    public BindCommand() {
        super("bind");
    }

    @Override
    public void execute(List<String> args) {
        if (args.size() == 3) {
            String moduleName = args.get(1);
            String key = args.get(2);

            @SuppressWarnings("all")
            Module module = ModuleManager.INSTANCE.getModuleByName(moduleName);

            if(module != null) {
                InputUtil.Key ikey = InputUtil.fromTranslationKey("key.keyboard." + key);
                module.setKey(ikey.getCode());
                ChatUtils.sendMsg("&7Bound &a" + module.getName() + "&7 to &a" + key, true);
            } else {
                ChatUtils.sendMsg("&cModule not found", true);
            }
        } else {
            ChatUtils.sendMsg("&cUsage: .bind <module> <key>", true);
        }
    }
}
