package me.stormcph.lumina.ui.clickgui;

import me.stormcph.lumina.module.Category;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {

    private final List<Tab> tabs = new ArrayList<>();
    public static ClickGui instance = new ClickGui();

    public ClickGui() {
        super(Text.of("Lumina Menu"));
        int offset = 40;
        for (Category value : Category.values()) {
            if(value == Category.CATEGORY_MNG) continue;
            tabs.add(new Tab(value, offset));
            offset += 240;
        }
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        //customFont = ClickGuiModu.customFont.isEnabled();

       // SimplifiedFontRenderer.drawString(matrices, new TextFont("comfortaa", 20, 0, 0, 0, Lang.ENG), "Test", 10, 10, Color.white);

        for (Tab tab : tabs) {
            tab.drawScreen(matrices, mouseX, mouseY);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        tabs.forEach(tab -> tab.mouseReleased(mouseX, mouseY, button));

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        tabs.forEach(tab -> tab.mouseClicked(mouseX, mouseY, button));

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {

        tabs.forEach(tab -> tab.charTyped(chr, modifiers));

        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        tabs.forEach(tab -> tab.keyPressed(keyCode, scanCode, modifiers));

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
