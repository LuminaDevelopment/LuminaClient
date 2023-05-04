package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.event.InputEvent;

public class LegitTotem extends Module {
    private final BooleanSetting totemHover = new BooleanSetting("TotemHover (WIP)", true);
    private PlayerEntity playerToTrack;
    private boolean totemUsed;

    public LegitTotem() {
        super("LegitTotem", "description", Category.GHOST);
        addSettings(totemHover);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        registerEvents();
    }

    private void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (totemHover.isEnabled()) {
                legitTotem(client);
            }
        });
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        // Your other code here
    }

    private void legitTotem(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return;

        if (player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            playerToTrack = player;
            totemUsed = false;
        } else if (playerToTrack == player && !totemUsed && client.currentScreen instanceof InventoryScreen) {
            PlayerScreenHandler screenHandler = player.playerScreenHandler;
            for (Slot slot : screenHandler.slots) {
                if (slot.getStack().getItem() == Items.TOTEM_OF_UNDYING) {
                    Point slotCenter = getSlotScreenPosition(client, slot);
                    GLFW.glfwSetCursorPos(client.getWindow().getHandle(), slotCenter.x, slotCenter.y);
                    totemUsed = true;
                    break;
                }
            }
        }
    }

    private Point getSlotScreenPosition(MinecraftClient client, Slot slot) {
        HandledScreen<?> handledScreen = (HandledScreen<?>) client.currentScreen;
        int guiLeft = (handledScreen.width - handledScreen.width) / 2;
        int guiTop = (handledScreen.height - handledScreen.height) / 2;

        int slotSize = 16;
        int slotSpacingX = 2;
        int slotSpacingY = 4;
        int leftOffset = guiLeft + 8;
        int topOffset = guiTop + 17;
        int column = slot.id % 9;
        int row = slot.id / 9;
        int x = leftOffset + (slotSize + slotSpacingX) * column + slotSize / 2;
        int y = topOffset + (slotSize + slotSpacingY) * row + slotSize / 2;

        return new Point(x, y);
    }

}
