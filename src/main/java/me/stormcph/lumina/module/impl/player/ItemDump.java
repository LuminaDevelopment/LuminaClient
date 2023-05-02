package me.stormcph.lumina.module.impl.player;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;


public class ItemDump extends Module {
    private final MinecraftClient client = MinecraftClient.getInstance();

    private final NumberSetting delay = new NumberSetting("Delay", 0.0, 1000.0, 150.0, 0.1);
    private TimerUtil timer;

    BooleanSetting genericContainerScreen = new BooleanSetting("GenericContainerScreen", true);
    BooleanSetting shulkerBoxScreen = new BooleanSetting("ShulkerBoxScreen", true);
    BooleanSetting hopperScreen = new BooleanSetting("HopperScreen", true);
    BooleanSetting generic3x3ContainerScreen = new BooleanSetting("Generic3x3ContainerScreen", true);
    BooleanSetting beacon = new BooleanSetting("Beacon", false);
    BooleanSetting loom = new BooleanSetting("Loom", false);
    BooleanSetting cartographyTable = new BooleanSetting("CartographyTable", false);
    BooleanSetting grindstoneScreen = new BooleanSetting("GrindstoneScreen", false);
    BooleanSetting anvil = new BooleanSetting("Anvil", false);
    BooleanSetting smoker = new BooleanSetting("Smoker", false);
    BooleanSetting blastFurnace = new BooleanSetting("BlastFurnace", false);
    BooleanSetting furnace = new BooleanSetting("Furnace", false);
    BooleanSetting brewingStand = new BooleanSetting("BrewingStand", false);
    BooleanSetting enchantmentTable = new BooleanSetting("EnchantmentTable", false);
    BooleanSetting stoneCutter = new BooleanSetting("StoneCutter", false);

    public ItemDump() {
        super("ItemDump (WIP)", "Dumps items into certain gui's", Category.MISC);
        addSettings(genericContainerScreen, shulkerBoxScreen, hopperScreen, generic3x3ContainerScreen, beacon, loom, cartographyTable, grindstoneScreen, anvil, smoker, blastFurnace, furnace, brewingStand, enchantmentTable, stoneCutter);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        timer = new TimerUtil();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        /*if (client.currentScreen instanceof HandledScreen<?> screen && isStorageScreen(screen)) {
            if (timer.hasReached(delay.getValue())) {
                for (int i = 0; i < client.player.getInventory().size(); i++) {
                    ItemStack item = client.player.getInventory().getStack(i);
                    if (item.isEmpty()) continue;

                    for (int j = 0; j < screen.getScreenHandler().getInventory().size(); j++) {
                        Slot targetSlot = screen.getScreenHandler().getSlot(j);
                        if (targetSlot.canInsert(item)) {
                            client.interactionManager.clickSlot(screen.getScreenHandler().syncId, i, 0, SlotActionType.QUICK_MOVE, client.player);
                            timer.reset();
                            return;
                        }
                    }
                }
            }
        }*/
    }

    private void onClientTick(MinecraftClient client) {
        Screen currentScreen = client.currentScreen;

        if (currentScreen != null && isStorageScreen(currentScreen)) {
            sendMsg("gui was detected");
        } else {
        }
    }

    private boolean isStorageScreen(Screen screen) {
        return
                (genericContainerScreen.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.GenericContainerScreen
                || (shulkerBoxScreen.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen
                || (hopperScreen.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.HopperScreen
                || (generic3x3ContainerScreen.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen
                || (beacon.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.BeaconScreen
                || (loom.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.LoomScreen
                || (cartographyTable.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.CartographyTableScreen
                || (grindstoneScreen.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.GrindstoneScreen
                || (anvil.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.AnvilScreen
                || (smoker.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.SmokerScreen
                || (blastFurnace.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.BlastFurnaceScreen
                || (furnace.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.FurnaceScreen
                || (brewingStand.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.BrewingStandScreen
                || (enchantmentTable.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.EnchantmentScreen
                || (stoneCutter.isEnabled()) && screen instanceof net.minecraft.client.gui.screen.ingame.StonecutterScreen;
    }

}
