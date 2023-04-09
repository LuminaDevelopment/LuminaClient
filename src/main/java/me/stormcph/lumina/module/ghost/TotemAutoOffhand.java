package me.stormcph.lumina.module.ghost;

import me.stormcph.lumina.module.Mod;
import me.stormcph.lumina.module.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;

public class TotemAutoOffhand extends Mod {

    private static final int OFFHAND_SLOT = 45;
    private static final int COOLDOWN_TICKS = 4; // 0.2 seconds at 20 ticks per second

    private int cooldown = 0;

    public TotemAutoOffhand() {
        super("TotemAutoOffhand", "Automatically moves totems to the offhand slot when the player opens their inventory.", Category.GHOST);
    }

    @Override
    public void onTick() {
        if (isEnabled()) {
            onStartTick(MinecraftClient.getInstance());
        }
        super.onTick();
    }

    public void onStartTick(MinecraftClient client) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        if (client.player == null || client.player.getInventory() == null || client.currentScreen != null) {
            return;
        }

        PlayerScreenHandler playerInventory = client.player.playerScreenHandler;
        int totemSlot = findTotemSlot(playerInventory);

        if (totemSlot != -1 && playerInventory.getSlot(OFFHAND_SLOT).hasStack()) {
            ItemStack totemStack = playerInventory.getSlot(totemSlot).getStack();
            ItemStack offhandStack = playerInventory.getSlot(OFFHAND_SLOT).getStack();

            playerInventory.getSlot(OFFHAND_SLOT).setStack(totemStack);
            playerInventory.getSlot(totemSlot).setStack(offhandStack);
            cooldown = COOLDOWN_TICKS;
        }
    }

    private int findTotemSlot(PlayerScreenHandler inventory) {
        for (int i = 0; i < inventory.slots.size(); i++) {
            if (inventory.getSlot(i).getStack().getItem() == Items.TOTEM_OF_UNDYING) {
                return i;
            }
        }
        return -1;
    }
}

