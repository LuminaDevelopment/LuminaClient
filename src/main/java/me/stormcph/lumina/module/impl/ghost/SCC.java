package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import net.minecraft.util.math.BlockPos;


import static me.stormcph.lumina.utils.PacketUtil.sendPacket;

public class SCC extends Module {
    private final BooleanSetting selfToggle = new BooleanSetting("Self Toggle?", true);
    private final TimerUtil cd = new TimerUtil();
    private final NumberSetting cooldown = new NumberSetting("cooldown-ms", 10.0, 1000.0, 50.0, 0.01);

    private boolean msgSent = false;
    private boolean hasAttacked = false;
    private boolean crystalBroken = false;
    private boolean crystalPlaced = false;

    public SCC() {
        super("SCC", "helps placing after knocking someone with a sword", Category.GHOST);
        addSettings(selfToggle, cooldown);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        msgSent = false;
    }

    @EventTarget
    private void onEvent(PacketSendEvent evnt) {
        HitResult hit = mc.crosshairTarget;
        if (!(hit instanceof EntityHitResult)) return;
        Entity target = ((EntityHitResult) hit).getEntity();
        Packet<?> packet = evnt.getPacket();

        if (packet instanceof PlayerInteractEntityC2SPacket && !(target instanceof EndCrystalEntity)) {
            hasAttacked = true;
            crystalBroken = false;
            crystalPlaced = false;
            cd.reset();
        }
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        checkRequiredItemsInHotbar();
        if (!crystalBroken) {
            crystalHit();
        }
        handleBlockPlacement();
    }

    private void crystalHit() {
        HitResult hit = mc.crosshairTarget;
        if (!(hit instanceof EntityHitResult)) return;
        Entity target = ((EntityHitResult) hit).getEntity();

        if (target instanceof EndCrystalEntity && cd.hasReached((int) cooldown.getValue())) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
            cd.reset();
            crystalBroken = true;
            if (selfToggle.isEnabled()) toggle();
        }
    }

    private int findSlotForItem(Item item) {
        for (int i = 0; i <= 8; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    private void checkRequiredItemsInHotbar() {
        int crystalSlot = findSlotForItem(Items.END_CRYSTAL);
        int obsidianSlot = findSlotForItem(Items.OBSIDIAN);

        if (crystalSlot >= 0 && obsidianSlot >= 0) {
            if (!msgSent) {
                sendMsg("crystal and obi found");
                msgSent = true;
            }
        } else {
            if (crystalSlot < 0) sendMsg("need crystal");
            if (obsidianSlot < 0) sendMsg("need obi");
            toggle();
        }
    }

    private boolean isBlockInCrosshair() {
        return mc.crosshairTarget instanceof BlockHitResult;
    }

    private void handleBlockPlacement() {
        if (!isBlockInCrosshair() || !hasAttacked || !cd.hasReached(cooldown.getValue()) || isObsidianInCrosshair()) {
            return;
        }

        int obsidianSlot = findSlotForItem(Items.OBSIDIAN);
        int crystalSlot = findSlotForItem(Items.END_CRYSTAL);

        if (mc.player.getInventory().selectedSlot != obsidianSlot) {
            mc.player.getInventory().selectedSlot = obsidianSlot;
            cd.reset();
        }

        if (cd.hasReached(cooldown.getValue()) && !isObsidianInCrosshair() && hasAttacked) {
            placeBlock();
            mc.player.getInventory().selectedSlot = crystalSlot;
            hasAttacked = false;
            cd.reset();
        }

        if (isObsidianInCrosshair() && cd.hasReached(cooldown.getValue()) && !crystalPlaced) {
            placeBlock();
            crystalPlaced = true;
            cd.reset();
        }
    }

    private void placeBlock() {
        BlockHitResult hitResult = (BlockHitResult) mc.crosshairTarget;
        mc.player.swingHand(mc.player.getActiveHand());
        sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hitResult, 0));
    }

    private boolean isObsidianInCrosshair() {
        if (mc.crosshairTarget instanceof BlockHitResult) {
            BlockPos blockPos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
            BlockState blockState = mc.world.getBlockState(blockPos);
            return blockState.getBlock() == Blocks.OBSIDIAN;
        }
        return false;
    }
}

