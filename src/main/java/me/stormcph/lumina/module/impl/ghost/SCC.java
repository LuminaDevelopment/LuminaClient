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
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
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
    public SCC() {
        super("SCC", "helps placing after knocking someone with a sword", Category.GHOST);
        addSettings(selfToggle);
        addSettings(cooldown);

    }
    private final BooleanSetting selfToggle = new BooleanSetting("Self Toggle?", true);
    private final TimerUtil cd = new TimerUtil();
    private final NumberSetting cooldown = new NumberSetting("cooldown-ms", 10.0, 1000.0, 50.0, 0.01);

    boolean msgSent = false;
    boolean hasAttacked = false;
    boolean crystalBroken = false;
    boolean crystalPlaced = false;
    @Override
    public void onEnable() {
        super.onEnable();
        msgSent = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    private void onEvent(PacketSendEvent evnt) {

        HitResult hit = mc.crosshairTarget;
        Entity target = ((EntityHitResult) hit).getEntity();
        Packet<?> packet = evnt.getPacket();
        if ((packet instanceof PlayerInteractEntityC2SPacket)&&!(target instanceof EndCrystalEntity)) {
            sendMsg("attacked");
            hasAttacked = true;
            sendMsg("1");
            crystalBroken = false;
            crystalPlaced = false;
            cd.reset();

        }
    }
    @EventTarget
    private void onUpdate(EventUpdate event) {
        ItemRequieredInHotbar();
        if (!crystalBroken) crystalHit();
        if (isBlockInCrosshair() && hasAttacked && cd.hasReached(cooldown.getValue()) && !ObsidianInCrosshair()) {
            sendMsg("block in crosshair");
            sendMsg("2");
            if (mc.player.getInventory().selectedSlot != ObsidianSlot()) {
                sendMsg("3");
                mc.player.getInventory().selectedSlot = ObsidianSlot();
                sendMsg("taken obsidian");
                cd.reset();
            }
            if (cd.hasReached(cooldown.getValue())&&!ObsidianInCrosshair()&&hasAttacked) {
                sendMsg("4");
                PlaceBlock();
                sendMsg("placed block ");
                mc.player.getInventory().selectedSlot = CrystalSlot();
                hasAttacked = false;
                cd.reset();
            }

        }
        if (ObsidianInCrosshair()&&cd.hasReached(cooldown.getValue())&&!crystalPlaced) {
            sendMsg("5");
            if (cd.hasReached(cooldown.getValue())) {
                sendMsg("placing crystal");
                PlaceBlock();
                crystalPlaced = true;
                cd.reset();
            }
        }
    }

    private void crystalHit(){
        HitResult hit = mc.crosshairTarget;
        if (hit.getType() != HitResult.Type.ENTITY)
            return;
        Entity target = ((EntityHitResult) hit).getEntity();
        if (!(target instanceof EndCrystalEntity))
            return;
        if (cd.hasReached((int) cooldown.getValue())) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
            cd.reset();
            crystalBroken = true;
            if (selfToggle.isEnabled()) toggle();
        }

    }
    //checking for crystal
    private int CrystalSlot() {
        for (int i = 0; i <= 8; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.END_CRYSTAL) {
                return (i);
            }
        }
        return (-1);
    }

    //checking for Obsidian
    private int ObsidianSlot() {

        for (int i = 0; i <= 8; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.OBSIDIAN) {
                return (i);
            }
        }
        return (-1);
    }



    //check your hotbar status and toggle off when requierement not met
    private void ItemRequieredInHotbar() {
        if ((CrystalSlot() >= 0) && (ObsidianSlot() >= 0)) {
            if (!msgSent) {
                sendMsg("crystal and obi found");
                msgSent = true;
            }
        } else if ((CrystalSlot() < 0) && (ObsidianSlot() < 0)) {
            sendMsg("need obsidian and crystals");
            toggle();
        } else if (CrystalSlot() < 0) {
            sendMsg("need crystal");
            toggle();
        } else if (ObsidianSlot() < 0) {
            sendMsg("need obi");
            toggle();
        }
    }

    private boolean isBlockInCrosshair() {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockHitResult hitResult = (BlockHitResult) mc.crosshairTarget;
        return hitResult != null && hitResult.getType() == BlockHitResult.Type.BLOCK;
    }

    private void PlaceBlock(){
        BlockHitResult hitResult = (BlockHitResult) mc.crosshairTarget;
        mc.player.swingHand(mc.player.getActiveHand());
        sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hitResult, 0));
    }

    private boolean ObsidianInCrosshair() {
        if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
            BlockState blockState = mc.world.getBlockState(blockPos);
            return blockState.getBlock() == Blocks.OBSIDIAN;
        }
        return false;
    }
}
