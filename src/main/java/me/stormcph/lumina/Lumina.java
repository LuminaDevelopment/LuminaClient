package me.stormcph.lumina;

import me.stormcph.lumina.event.EventManager;
import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.KeyEvent;
import me.stormcph.lumina.event.impl.Render2DEvent;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.ui.HudConfigScreen;
import me.stormcph.lumina.ui.screens.clickgui.ClickGui;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class Lumina implements ModInitializer {

    private static final Lumina INSTANCE = new Lumina();
    private final Logger logger = LogManager.getLogger(Lumina.class);

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onInitialize() {
        EventManager.register(this);
    }


    @EventTarget
    public void onKeyPress(KeyEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            for (Module module : ModuleManager.INSTANCE.getModules()) {
                if (event.getKey() == module.getKey()) module.toggle();
            }

            if (event.getKey() == GLFW.GLFW_KEY_RIGHT_ALT) mc.setScreen(ClickGui.INSTANCE);
            if (event.getKey() == GLFW.GLFW_KEY_H) mc.setScreen(new HudConfigScreen());
        }
    }

    @EventTarget
    public void onRender2D(Render2DEvent e) {
        if(mc.player != null) {
            for(Module module : ModuleManager.INSTANCE.getEnabledModules()) {
                if(module instanceof HudModule hudModule) {
                    hudModule.draw(e.getMatrices());
                }
            }
        }
    }

    public static Lumina getInstance() {
        return INSTANCE;
    }

    public Logger getLogger() {
        return logger;
    }
}
