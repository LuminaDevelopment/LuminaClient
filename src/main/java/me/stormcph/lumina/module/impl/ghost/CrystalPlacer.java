package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import static me.stormcph.lumina.utils.PacketUtil.sendPacket;

public class CrystalPlacer extends Module {
    public CrystalPlacer() {
        super("Crystal Placer", "helps placing crystal when looking at obsidian", Category.GHOST);
        addSettings(cooldown);
    }
    private final NumberSetting cooldown = new NumberSetting("cooldown-ms", 0.0, 1000.0, 50.0, 0.01);
    private final TimerUtil timerUtil = new TimerUtil();
    @EventTarget
    public void onUpdate(EventUpdate e) {
        if ((isObsidianInCrosshair())&&mc.player.getInventory().selectedSlot == CrystalSlot()&&(timerUtil.hasReached(cooldown.getValue()))) {
            PlaceBlock();
            timerUtil.reset();
        }
    }

    private boolean isObsidianInCrosshair() {
        if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();
            BlockState blockState = mc.world.getBlockState(blockPos);
            return blockState.getBlock() == Blocks.OBSIDIAN;
        }
        return false;
    }
    private void PlaceBlock(){
        BlockHitResult hitResult = (BlockHitResult) mc.crosshairTarget;
        mc.player.swingHand(mc.player.getActiveHand());
        sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hitResult, 0));
    }
    private int CrystalSlot() {
        for (int i = 0; i <= 8; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.END_CRYSTAL) {
                return (i);
            }
        }
        return (-1);
    }
}
