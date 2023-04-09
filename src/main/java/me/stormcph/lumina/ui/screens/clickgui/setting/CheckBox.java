package me.stormcph.lumina.ui.screens.clickgui.setting;

import me.stormcph.lumina.module.settings.BooleanSetting;
import me.stormcph.lumina.module.settings.Setting;
import me.stormcph.lumina.ui.screens.clickgui.ModuleButton;
import net.minecraft.client.util.math.MatrixStack;

public class CheckBox extends Component {

    private BooleanSetting boolSet = (BooleanSetting)setting;

    public CheckBox(Setting setting, ModuleButton parent, int offset) {
        super(setting, parent, offset);
        this.boolSet = (BooleanSetting)setting;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }
}
