package me.stormcph.lumina.module.impl.misc;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;

public class NoTrace extends Module {
    public NoTrace() {
        super("NoTrace", "Disables console logs, chat notifications, and all render modules", Category.MISC);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        for(Module m : ModuleManager.INSTANCE.getModulesByCategory(Category.RENDER)) m.setEnabled(false);
    }

    public static boolean shouldLog(){
        return !ModuleManager.INSTANCE.getModuleByClass(NoTrace.class).isEnabled();
    }

}
