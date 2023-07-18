package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.ui.clickgui.ClickGui;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;

public class ClickguiModule extends Module {

    public static BooleanSetting performance = new BooleanSetting("Performance", false);
    public static ModeSetting clickguiMode = new ModeSetting("Clickgui Mode", "New", "New", "Old");

    public ClickguiModule() {
        super("ClickGui", "Opens the module selection/editing screen", Category.RENDER);
        this.addSettings(performance, clickguiMode);
        setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if(!(mc.currentScreen instanceof ClickGui) && !(mc.currentScreen instanceof me.stormcph.lumina.ui.old_ui.ClickGui)) {
            switch (ClickguiModule.clickguiMode.getMode()) {
                case "New" -> {
                    mc.setScreen(ClickGui.instance);
                }
                case "Old" -> {
                    mc.setScreen(me.stormcph.lumina.ui.old_ui.ClickGui.INSTANCE);
                }
                default -> {
                    JOptionPane.showMessageDialog(null, "How the fuck did you manage this", "Invalid ClickGUI Mode", JOptionPane.ERROR_MESSAGE);
                    System.out.println("How the fuck did you manage this");
                }
            }
        }
        toggle();
    }
}
