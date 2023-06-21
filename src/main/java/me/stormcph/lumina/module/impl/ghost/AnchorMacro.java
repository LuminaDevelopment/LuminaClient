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
    private final TimerUtil timer;
    private ActionState state;

    public enum ActionState {
        IDLE,
        CHARGING,
        EXPLODING
    }

    public AnchorMacro() {
        super("AnchorMacro", "Fills and explodes anchors", Category.COMBAT);
        chargeOnly = new BooleanSetting("Charge Only", false);
        cooldown = new NumberSetting("Cooldown between charges", 0, 10, 4, 1);
        addSettings(chargeOnly, cooldown);
        timer = new TimerUtil();
        state = ActionState.IDLE;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        state = ActionState.IDLE;
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

        if (nullCheck() || mc.player.isUsingItem() || GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 1) != 1) {
            System.out.println("[DEBUG] Update event: Either null check failed, player is using item, or mouse button is not pressed.");
            state = ActionState.IDLE;
            return;
        }
        if (mc.crosshairTarget instanceof BlockHitResult blockHit) {
            System.out.println("[DEBUG] Update event: Crosshair target is an instance of BlockHitResult.");
            BlockPos pos = blockHit.getBlockPos();
            switch (state) {
                case IDLE:
                    if (isUnCharged(pos)) {
                        System.out.println("[DEBUG] Update event: Found uncharged anchor, proceeding to charge.");
                        selectItem(Items.GLOWSTONE);
                        interact(blockHit);
                        state = ActionState.CHARGING;
                    }
                    break;
                case CHARGING:
                    if (isCharged(pos) && !chargeOnly.isEnabled()) {
                        System.out.println("[DEBUG] Update event: Found charged anchor, proceeding to explode.");
                        selectItem(Items.RESPAWN_ANCHOR);
                        interact(blockHit);
                        state = ActionState.EXPLODING;
                        timer.reset();
                    }
                    break;
                case EXPLODING:
                    if (timer.hasReached(cooldown.getFloatValue() * 1000)) {
                        System.out.println("[DEBUG] Update event: Cooldown reached, resetting state.");
                        state = ActionState.IDLE;
                    }
                    break;
            }
        } else {
            System.out.println("[DEBUG] Update event: Crosshair target is not an instance of BlockHitResult.");
        }
    }

    private void interact(BlockHitResult blockHit) {
        System.out.println("[DEBUG] Interact: Trying to interact with block at " + blockHit.getBlockPos());
        if (mc.world.getBlockState(blockHit.getBlockPos()).getBlock() instanceof RespawnAnchorBlock) {
            System.out.println("[DEBUG] Interact: Block at target location is a RespawnAnchorBlock.");
        } else {
            System.out.println("[DEBUG] Interact: Block at target location is not a RespawnAnchorBlock.");
        }
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit);
        mc.player.swingHand(Hand.MAIN_HAND);
    }


    private boolean isUnCharged(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        if (!(state.getBlock() instanceof RespawnAnchorBlock)) {
            System.out.println("Block is not a RespawnAnchorBlock");
            return false;
        }
        boolean uncharged = state.get(RespawnAnchorBlock.CHARGES) == 0;
        System.out.println("Block is " + (uncharged ? "uncharged" : "charged"));
        return uncharged;
    }

    private boolean isCharged(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        if (!(state.getBlock() instanceof RespawnAnchorBlock)) {
            System.out.println("Block is not a RespawnAnchorBlock");
            return false;
        }
        boolean charged = state.get(RespawnAnchorBlock.CHARGES) != 0;
        System.out.println("Block is " + (charged ? "charged" : "uncharged"));
        return charged;
    }

    private void selectItem(Item item) {
        System.out.println("Attempting to select " + item.getTranslationKey());
        PlayerInventory inv = mc.player.getInventory();
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (item.equals(itemStack.getItem())) {
                System.out.println("Item found in slot " + i);
                inv.selectedSlot = i;
                break;
            }
        }
    }
}
