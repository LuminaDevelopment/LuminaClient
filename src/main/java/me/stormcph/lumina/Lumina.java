package me.stormcph.lumina;

import me.stormcph.lumina.cape.CapeManager;
import me.stormcph.lumina.ui.clickgui.ClickGui;
import me.stormcph.lumina.config.ConfigReader;
import me.stormcph.lumina.event.EventManager;
import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.KeyEvent;
import me.stormcph.lumina.event.impl.Render2DEvent;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.ClickguiModule;
import me.stormcph.lumina.ui.HudConfigScreen;
import me.stormcph.lumina.utils.misc.GithubRetriever;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;

public class Lumina implements ClientModInitializer {

    private static final Lumina INSTANCE = new Lumina();
    private final Logger logger = LogManager.getLogger(Lumina.class);

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        EventManager.register(this);
        CapeManager.init();
        new GithubRetriever().retrieve();
       // SessionChanger.loginCracked("LuminaUser");
        getLogger().info("Set username to LuminaUser");


        Thread configThr = new Thread(ConfigReader::loadConfig, "LuminaConfigReaderThread");
        configThr.start();
    }

    @EventTarget
    public void onKeyPress(KeyEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            for (Module module : ModuleManager.INSTANCE.getModules()) {
                if(MinecraftClient.getInstance().currentScreen!=null) return;
                if (event.getKey() == module.getKey()) module.toggle();
            }

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
