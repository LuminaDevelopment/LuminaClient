package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.Random;

import static me.stormcph.lumina.utils.PacketUtil.sendPacket;

public class CrystalPopAndPlace extends Module {
    private final TimerUtil timerUtil = new TimerUtil();
    private final NumberSetting cooldownPlace = new NumberSetting("Place Cooldown-ms", 0.0, 1000.0, 50.0, 0.01);
    private final NumberSetting cooldownPop = new NumberSetting("Pop Cooldown-ms", 0.0, 1000.0, 0.0, 0.01);

    private final BooleanSetting onlyOwnCrystal = new BooleanSetting("OnlyOwnCrystal", false);
    private final BooleanSetting tryPunch = new BooleanSetting("DonutSmpBypass", false);
    private boolean playerPlacedCrystal = false;

    private final BooleanSetting preserveItems = new BooleanSetting("NoLootPop", true);
    private final NumberSetting lootProtectRadiusX = new NumberSetting("ProtectX", 0.0, 16.0, 8.0, 0.01);
    private final NumberSetting lootProtectRadiusY = new NumberSetting("ProtectY", 0.0, 16.0, 8.0, 0.01);
    private final NumberSetting lootProtectRadiusZ = new NumberSetting("ProtectZ", 0.0, 16.0, 8.0, 0.01);

    private final BooleanSetting smallRandomization = new BooleanSetting("SmallRandomization", false);
    private final BooleanSetting mediumRandomization = new BooleanSetting("MediumRandomization", false);
    private final BooleanSetting largeRandomization = new BooleanSetting("LargeRandomization", false);
    private final Random random = new Random();

    public CrystalPopAndPlace() {
        super("CrystalSpam", "Automatically places and pops end crystal", Category.GHOST);
        addSettings(cooldownPlace, cooldownPop, onlyOwnCrystal, /*tryPunch,*/ preserveItems, lootProtectRadiusX, lootProtectRadiusY, lootProtectRadiusZ, smallRandomization, mediumRandomization, largeRandomization);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        endCrystalTrigger();
        trackPlacedCrystals();
        placeCrystal();
    }

    private void endCrystalTrigger() {
        Entity target = raycastEndCrystal(5);
        if (target == null)
            return;

        if (onlyOwnCrystal.isEnabled() && !playerPlacedCrystal)
            return;

        if (preserveItems.isEnabled() && ItemNearby(target, 6))
            return;

        if (timerUtil.hasReached((int) getCooldownValueWithRandomization(cooldownPop.getValue()))) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
            timerUtil.reset();
            playerPlacedCrystal = false;
        }
    }

    private double getCooldownValueWithRandomization(double baseValue) {
        int randomizationValue = 0;
        if (smallRandomization.isEnabled() && random.nextBoolean()) {
            randomizationValue = random.nextInt(11) - 5; // random number between -5 and 5
        }
        else if (mediumRandomization.isEnabled() && random.nextBoolean()) {
            randomizationValue = random.nextInt(31) - 15; // random number between -15 and 15
        }
        else if (largeRandomization.isEnabled() && random.nextBoolean()) {
            randomizationValue = random.nextInt(51) - 25; // random number between -25 and 25
        }

        return baseValue + randomizationValue;
    }


    private void trackPlacedCrystals() {
        if (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL && mc.options.useKey.isPressed()) {
            playerPlacedCrystal = true;
        }
    }

    private Entity raycastEndCrystal(double range) {
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
        Vec3d viewVector = mc.player.getRotationVecClient();
        Vec3d extendedPoint = cameraPos.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof EndCrystalEntity) {
                if (entity.getBoundingBox().expand(0.3).intersects(cameraPos, extendedPoint)) {
                    return entity;
                }
            }
        }

        return null;
    }

    private boolean ItemNearby(Entity entity, double range) {
        Box boundingBox = new Box(
                entity.getPos().x - lootProtectRadiusX.getValue(),
                entity.getPos().y - lootProtectRadiusY.getValue(),
                entity.getPos().z - lootProtectRadiusZ.getValue(),
                entity.getPos().x + lootProtectRadiusX.getValue(),
                entity.getPos().y + lootProtectRadiusY.getValue(),
                entity.getPos().z + lootProtectRadiusZ.getValue()
        );

        for (Entity nearbyEntity : mc.world.getOtherEntities(null, boundingBox)) {
            if (nearbyEntity instanceof ItemEntity) {
                if (isPreciousItem(((ItemEntity) nearbyEntity).getStack().getItem())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isPreciousItem(Item item) {
        return item == Items.DIAMOND ||
                item == Items.DIAMOND_BLOCK ||
                item == Items.DIAMOND_SWORD ||
                item == Items.DIAMOND_PICKAXE ||
                item == Items.DIAMOND_AXE ||
                item == Items.DIAMOND_SHOVEL ||
                item == Items.DIAMOND_HOE ||
                item == Items.DIAMOND_HELMET ||
                item == Items.DIAMOND_CHESTPLATE ||
                item == Items.DIAMOND_LEGGINGS ||
                item == Items.DIAMOND_BOOTS ||
                item == Items.NETHERITE_INGOT ||
                item == Items.NETHERITE_BLOCK ||
                item == Items.NETHERITE_SWORD ||
                item == Items.NETHERITE_PICKAXE ||
                item == Items.NETHERITE_AXE ||
                item == Items.NETHERITE_SHOVEL ||
                item == Items.NETHERITE_HOE ||
                item == Items.NETHERITE_HELMET ||
                item == Items.NETHERITE_CHESTPLATE ||
                item == Items.NETHERITE_LEGGINGS ||
                item == Items.NETHERITE_BOOTS;
    }

    private void placeCrystal() {
        if ((isObsidianInCrosshair()) && mc.player.getInventory().selectedSlot == CrystalSlot() && (timerUtil.hasReached(getCooldownValueWithRandomization(cooldownPlace.getValue())))) {
            placeBlock();
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

    private void placeBlock(){
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
