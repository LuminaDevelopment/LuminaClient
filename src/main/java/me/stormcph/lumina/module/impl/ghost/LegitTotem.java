package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

import java.util.OptionalInt;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LegitTotem extends Module implements ClientTickEvents.StartTick {
    private final Item itemToCheck;
    private final Random random;
    private final ScheduledExecutorService executorService;
    private final AtomicBoolean actionScheduled;
    private boolean wasInventoryOpen;

    public LegitTotem(Item itemToCheck) {
        super("Legit Totem", "When you go into inventory it automatically puts totem in offhand", Category.GHOST);

        this.itemToCheck = itemToCheck;
        this.random = new Random();
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.actionScheduled = new AtomicBoolean(false);
        this.wasInventoryOpen = false;
    }

    @EventTarget
    public void onTick(EventUpdate e) {
        onStartTick(mc);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onStartTick(MinecraftClient client) {
        if (client.player != null) {
            boolean isInventoryOpen = client.currentScreen != null && client.currentScreen.passEvents;

            if (!wasInventoryOpen && isInventoryOpen && !actionScheduled.get()) {
                PlayerInventory inventory = client.player.getInventory();
                OptionalInt totemSlotIndexOpt = findFirstItemSlotIndex(inventory, itemToCheck);

                if (totemSlotIndexOpt.isPresent() && inventory.offHand.get(0).isEmpty()) {
                    actionScheduled.set(true);
                    int cooldown = 121 + random.nextInt(33); // Random cooldown between 121 and 153 ms

                    executorService.schedule(() -> {
                        // Move the totem to the offhand slot
                        client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, totemSlotIndexOpt.getAsInt(), 0, SlotActionType.PICKUP, client.player);
                        client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, 45, 0, SlotActionType.PICKUP, client.player);

                        System.out.printf("Totem of Undying placed in player's offhand with a %d ms delay!%n", cooldown);
                        actionScheduled.set(false);
                    }, cooldown, TimeUnit.MILLISECONDS);
                }
            }

            wasInventoryOpen = isInventoryOpen;
        }
    }

    private OptionalInt findFirstItemSlotIndex(PlayerInventory inventory, Item item) {
        // Check only main inventory slots (0-35)
        for (int i = 0; i < 36; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() == item) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }
}
