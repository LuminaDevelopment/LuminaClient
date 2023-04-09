package me.stormcph.lumina;

import ca.weblite.objc.Client;
import me.stormcph.lumina.module.Mod;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.ui.screens.clickgui.ClickGui;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class Lumina implements ModInitializer {

    public static final Lumina INSTANCE = new Lumina();
    public Logger logger = LogManager.getLogger(Lumina.class);

    private MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onInitialize() {
    }

    public void onKeyPress(int key, int action) {
        if (action == GLFW.GLFW_PRESS) {
            for (Mod module : ModuleManager.ISTANCE.getModules()) {
                if (key == module.getKey()) module.toggle();
            }

            if (key == GLFW.GLFW_KEY_RIGHT_ALT) mc.setScreen(ClickGui.INSTANCE);
        }
    }


    public void onTick() {
        if (mc.player != null) {
            for (Mod module : ModuleManager.ISTANCE.getEnabledModules()) {
                module.onTick();
            }

        }
    }

}
