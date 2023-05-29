package me.stormcph.lumina.mixins;

import me.stormcph.lumina.clickgui.Tab;
import me.stormcph.lumina.module.Category;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {
    private MinecraftClient mc = MinecraftClient.getInstance();
    private Tab tab = new Tab(Category.SERVER_SCANNER, 40, 20, 115 * (float)mc.getWindow().getScaleFactor(), (mc.textRenderer.fontHeight + 10) * (float)mc.getWindow().getScaleFactor());
    public MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "render", cancellable = true)
    void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        tab.setWidth(115 * (float)mc.getWindow().getScaleFactor());
        tab.setHeight((mc.textRenderer.fontHeight + 10) * (float)mc.getWindow().getScaleFactor());
        tab.drawScreen(matrices, mouseX, mouseY, tab.getX() + tab.getWidth() / 2 - mc.textRenderer.getWidth(Category.SERVER_SCANNER.name) * (float)mc.getWindow().getScaleFactor() / 2);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {

        tab.mouseReleased(mouseX, mouseY, button);

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        tab.mouseClicked(mouseX, mouseY, button);

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {

        tab.charTyped(chr, modifiers);

        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        tab.keyPressed(keyCode, scanCode, modifiers);

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
