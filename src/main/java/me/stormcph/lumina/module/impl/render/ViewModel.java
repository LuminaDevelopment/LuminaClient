package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ViewModel extends Module {

    private final NumberSetting xT = new NumberSetting("Translate X", -10, 10, 0, 0.1);
    private final NumberSetting yT = new NumberSetting("Translate Y", -10, 10, 0, 0.1);
    private final NumberSetting zT = new NumberSetting("Translate Z", -10, 10, 0, 0.1);

    private final NumberSetting xSc = new NumberSetting("Scale X", -2, 2, 1, 0.1);
    private final NumberSetting ySc = new NumberSetting("Scale Y", -2, 2, 1, 0.1);
    private final NumberSetting zSc = new NumberSetting("Scale Z", -2, 2, 1, 0.1);

    public ViewModel() {
        super("ViewModel", "Change the way held items are rendered", Category.RENDER);
        addSettings(xT, yT, zT, xSc, ySc, zSc);
    }

    public void render(ItemStack stack, MatrixStack matrices) {
        matrices.translate(xT.getFloatValue(), yT.getFloatValue(), zT.getFloatValue());
        matrices.scale(xSc.getFloatValue(), ySc.getFloatValue(), zSc.getFloatValue());
    }
}
