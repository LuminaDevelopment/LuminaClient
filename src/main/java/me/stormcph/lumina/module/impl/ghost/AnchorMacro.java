package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.time.TimerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

public class AnchorMacro extends Module {

    private final BooleanSetting chargeOnly;
    private final NumberSetting cooldown;
    private boolean hasAnchored;
    private final TimerUtil timer;

    public AnchorMacro() {
        super("AnchirMacro", "A macro for using Respawn Anchors", Category.PLAYER);
        chargeOnly = new BooleanSetting("Charge Only", false);
        cooldown = new NumberSetting("Cooldown between charges", 0, 10, 4, 1);
        addSettings(chargeOnly, cooldown);
        timer = new TimerUtil();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        hasAnchored = false;
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

        if (nullCheck() || mc.player.isUsingItem() || GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 1) != 1) {
            return;
        }
        if (mc.crosshairTarget instanceof BlockHitResult blockHit) {
            // Pointer to the block position
            BlockPos pos = blockHit.getBlockPos();
            if (hasAnchored && !timer.hasReached(cooldown.getFloatValue() * 1000)) {
                return;
            }
            if (isUnCharged(pos)) {
                // Charge the anchor
                if (mc.player.isHolding(Items.GLOWSTONE)) {
                    interact(blockHit);
                    return;
                }
                // Switch to glowstone
                selectItem(Items.GLOWSTONE);
                interact(blockHit);
            }
            else if (isCharged(pos) && !chargeOnly.isEnabled()) {
                selectItem(Items.RESPAWN_ANCHOR);
                interact(blockHit);
                hasAnchored = true;
                timer.reset();
            }
        }
    }

    private void interact(BlockHitResult blockHit) {
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private boolean isUnCharged(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        if (!(state.getBlock() instanceof RespawnAnchorBlock)) {
            return false;
        }
        return state.get(RespawnAnchorBlock.CHARGES) == 0;
    }

    private boolean isCharged(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        if (!(state.getBlock() instanceof RespawnAnchorBlock)) {
            return false;
        }
        return state.get(RespawnAnchorBlock.CHARGES) != 0;
    }

    private void selectItem(Item item) {
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (item.equals(itemStack.getItem())) {
                inv.selectedSlot = i;
                break;
            }
        }
    }
}