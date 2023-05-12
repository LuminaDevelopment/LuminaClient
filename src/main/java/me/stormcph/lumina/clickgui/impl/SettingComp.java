package me.stormcph.lumina.clickgui.impl;

import me.stormcph.lumina.clickgui.Component;
import me.stormcph.lumina.setting.Setting;
import net.minecraft.client.util.math.MatrixStack;

public abstract class SettingComp extends Component.PanelComponent {

    protected double y;
    private final Setting setting;
    protected final ModuleButton parent;

    protected SettingComp(Setting setting, ModuleButton parent) {
        this.setting = setting;
        this.parent = parent;
    }

    @Override
    public void drawScreen(MatrixStack matrices, double mouseX, double mouseY, double y) {
        this.y = y;
    }

    public Setting getSetting() {
        return setting;
    }
}
