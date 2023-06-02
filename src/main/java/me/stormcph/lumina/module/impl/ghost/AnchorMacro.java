package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AnchorMacro extends Module {

    //private final BooleanSetting switchToAnchor = new BooleanSetting("SwitchToAnchor", true); this setting is a bit stupid since why is it a macro then? and its way too much work to make it listen for when the player is holding the anchor
    private final BooleanSetting placeAnchor = new BooleanSetting("PlaceAnchor", true);
    private final NumberSetting chargeAmount = new NumberSetting("chargeAmount", 0, 4, 2, 1);
    private final BooleanSetting popAnchor = new BooleanSetting("PopAnchor", true);
    private final BooleanSetting back = new BooleanSetting("SwitchBack", true); // this is nice though
    private final BooleanSetting debug = new BooleanSetting("debug", false);

    public AnchorMacro() {
        super("AnchorMacro", "does the following steps (based on config) 1. place anchor 2. charge anchor 3. pop anchor (semi-ghost)", Category.GHOST);
        addSettings(placeAnchor, chargeAmount, popAnchor, back, debug);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        PlayerEntity player = mc.player;
        BlockPos pos = player.getBlockPos();
        Hand hand = Hand.MAIN_HAND;
        AnchorMacro(player, pos, hand);
    }

    @EventTarget
    public void onUpdate() {
    }

    private void AnchorMacro(PlayerEntity player, BlockPos pos, Hand hand) {
        ItemStack handStack = player.getMainHandStack();
        HitResult hitResult = mc.crosshairTarget;
        int originalSlot = mc.player.getInventory().selectedSlot;

        for (int i = 0; i < 9; i++) {
            ItemStack stackInSlot = mc.player.getInventory().getStack(i);
            if (stackInSlot.getItem() == Items.RESPAWN_ANCHOR) {
                if (debug.isEnabled()) {
                    sendMsg("Respawn-Anchor was found!");
                }
                mc.player.getInventory().selectedSlot = i;
            }
        }
        BlockPos anchorPos = null;
        if (placeAnchor.isEnabled()) {
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                anchorPos = blockHitResult.getBlockPos();

                if (mc.world != null && mc.interactionManager != null) {
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    if (debug.isEnabled()) {
                        sendMsg("placed Respawn-Anchor");
                    }
                }
            }
        }
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();

            for (int i = 0; i < 9; i++) {
                ItemStack stackInSlot = mc.player.getInventory().getStack(i);
                if (stackInSlot.getItem() == Items.GLOWSTONE) {
                    if (debug.isEnabled()) {
                        sendMsg("Glowstone was found!");
                    }
                    mc.player.getInventory().selectedSlot = i;
                    handStack = mc.player.getMainHandStack();
                }
            }

            if (handStack.getItem() == Items.GLOWSTONE && handStack.getCount() >= 3) {
                if (debug.isEnabled()) { sendMsg("Glowstone is valid"); }
                for (int i = 0; i < 3; i++) {
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(player.getPos(), Direction.UP, blockPos, false));
                }
                if (debug.isEnabled()) { sendMsg("Anchor was charged with glowstone"); }
            }
        }

    }
}
/*
* process tree:
* 1. switch to hotbar slot with respawn anchor present (private final BooleanSetting switchToAnchor = new BooleanSetting("SwitchToAnchor", true);)
* 2. place respawn anchor where the player is looking
* 3. charge the anchor either 1, 2, 3 or 4 times
* 4. pop the anchor
* 5. switch back to the original hotbar slot (private final BooleanSetting back = new BooleanSetting("SwitchBack", true);)
* written by Stormcph
*/