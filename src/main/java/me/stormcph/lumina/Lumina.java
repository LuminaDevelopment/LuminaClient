package me.stormcph.lumina;

import me.stormcph.lumina.cape.CapeManager;
import me.stormcph.lumina.clickgui.ClickGui;
import me.stormcph.lumina.config.ConfigReader;
import me.stormcph.lumina.event.EventManager;
import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.KeyEvent;
import me.stormcph.lumina.event.impl.Render2DEvent;
import me.stormcph.lumina.module.HudModule;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.ClickguiModule;
import me.stormcph.lumina.old_ui.HudConfigScreen;
import me.stormcph.lumina.utils.GithubRetriever;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;

public class Lumina implements ModInitializer {

    private static final Lumina INSTANCE = new Lumina();
    private final Logger logger = LogManager.getLogger(Lumina.class);

    private final MinecraftClient mc = MinecraftClient.getInstance();

    private static final KeyBinding openClickGuiKey = new KeyBinding("key.lumina.open_click_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_ALT, "category.lumina");
    private static final KeyBinding openHudConfigScreenKey = new KeyBinding("key.lumina.open_hud_config_screen", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, "category.lumina");

    @Override
    public void onInitialize() {
        EventManager.register(this);
        CapeManager.init();
        new GithubRetriever().retrieve();

        KeyBindingHelper.registerKeyBinding(openClickGuiKey);
        KeyBindingHelper.registerKeyBinding(openHudConfigScreenKey);

       // SessionChanger.loginCracked("LuminaUser");
        System.out.println("Set username to LuminaUser");


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

            if (event.matches(openClickGuiKey)) {
                switch (ClickguiModule.clickguiMode.getMode()) {
                    case "New" -> {
                        mc.setScreen(ClickGui.instance);
                    }
                    case "Old" -> {
                        mc.setScreen(me.stormcph.lumina.old_ui.screens.clickgui.ClickGui.INSTANCE);
                    }
                    default -> {
                        JOptionPane.showMessageDialog(null, "How the fuck did you manage this", "Invalid ClickGUI Mode", JOptionPane.ERROR_MESSAGE);
                        System.out.println("How the fuck did you manage this");
                    }
                }

            }
            if (openHudConfigScreenKey.wasPressed()) mc.setScreen(new HudConfigScreen());
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
