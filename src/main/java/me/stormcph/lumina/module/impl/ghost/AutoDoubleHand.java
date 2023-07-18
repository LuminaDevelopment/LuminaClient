package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.PlayerInteractItemEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class AutoDoubleHand extends Module {

    private final TimerUtil timerUtil = new TimerUtil();
    private Item previousOffhandItem = Items.AIR;


    private final BooleanSetting enderPearlPredict = new BooleanSetting("PredictPearlDamage", true);
    private final NumberSetting healthIndicator = new NumberSetting("health", 0.0, 36, 0, 1);
    private final NumberSetting cooldown = new NumberSetting("SwitchDelay", 0.0, 10000.0, 50.0, 0.01);
    private final NumberSetting crystalRadiusX = new NumberSetting("Crystal X", 0.0, 16.0, 8.0, 0.01);
    private final NumberSetting crystalRadiusYPlus = new NumberSetting("Crystal (above) Y", 0.0, 16.0, 5.0, 0.01);
    private final NumberSetting crystalRadiusYMinus = new NumberSetting("Crystal (below) Y", 0.0, 1.0, 0.6, 0.01);
    private final NumberSetting crystalRadiusZ = new NumberSetting("Crystal Z", 0.0, 16.0, 8.0, 0.01);


    private final NumberSetting anchorRadiusX = new NumberSetting("Anchor X", 0.0, 8.0, 3.0, 0.01);
    private final NumberSetting anchorRadiusYPlus = new NumberSetting("Anchor (above) Y", 0.0, 8.0, 3.0, 0.01);
    private final NumberSetting anchorRadiusYMinus = new NumberSetting("Anchor (below) Y", 0.0, 8.0, 3.0, 0.01);
    private final NumberSetting anchorRadiusZ = new NumberSetting("Anchor Z", 0.0, 8.0, 3.0, 0.01);
    private final BooleanSetting onlyCharged = new BooleanSetting("only if charged", false);
    private final BooleanSetting obsidianAnchorCheck = new BooleanSetting("Obsidian Anchor Check", true);
    private final BooleanSetting antiFall = new BooleanSetting("AntiFall", true);
    private final BooleanSetting offhandPop = new BooleanSetting("OffhandPop", false);


    public AutoDoubleHand() {
        super("AutoDoubleHand", "holds a totem in mainhand when a certain action happens", Category.GHOST);
        addSettings(/*enderPearlPredict, */
                healthIndicator,
                antiFall,
                offhandPop,
                cooldown,
                crystalRadiusX,
                crystalRadiusYPlus,
                crystalRadiusYMinus,
                crystalRadiusZ,
                anchorRadiusX,
                anchorRadiusYPlus,
                anchorRadiusYMinus,
                anchorRadiusZ,
                onlyCharged,
                obsidianAnchorCheck);
    }


    @Override
    public void onDisable() {
        super.onDisable();

    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if(nullCheck()) return;
        ClientPlayerEntity player = mc.player;
        checkEndCrystal(player);
        anchorCheck(player);
        checkHealth(player);
        antiFall(player);

        // Check for offhand pop
        if (offhandPop.isEnabled()) {
            handleOffhandPop(player);
        }
    }

    @EventTarget
    public void onPearlThrow(PlayerInteractItemEvent e) {
        if(nullCheck()) return;
        if(e.getPlayer().getStackInHand(e.getHand()).getItem() instanceof EnderPearlItem) {
           // predictDamage(e.getPlayer(), e.getPlayer().ray);
        }
    }

    private Optional<BlockPos> getObsidianBlockingAnchor(ClientPlayerEntity player, BlockPos anchorPos) {
        BlockPos[] surroundingBlocks = new BlockPos[]{
                anchorPos.up(),
                anchorPos.north(),
                anchorPos.east(),
                anchorPos.south(),
                anchorPos.west()
        };

        for (BlockPos pos : surroundingBlocks) {
            BlockState blockState = player.world.getBlockState(pos);
            if (blockState.getBlock() == Blocks.OBSIDIAN) {
                BlockPos.Mutable checkPos = new BlockPos.Mutable();
                checkPos.set(pos);
                while (!checkPos.equals(anchorPos)) {
                    checkPos.move(Direction.getFacing(anchorPos.getX() - pos.getX(), anchorPos.getY() - pos.getY(), anchorPos.getZ() - pos.getZ()));
                    BlockState checkState = player.world.getBlockState(checkPos);
                    if (checkState.isAir() || !checkState.isOpaque()) {
                        return Optional.empty();
                    }
                }
                return Optional.of(pos);
            }
        }

        return Optional.empty();
    }


    private void checkEndCrystal(ClientPlayerEntity player) {
        double radiusX = crystalRadiusX.getValue();
        double radiusY = crystalRadiusYPlus.getValue();
        double radiusZ = crystalRadiusZ.getValue();
        double radiusBelowY = crystalRadiusYMinus.getValue();

        double minY = player.getY() - radiusY;
        double maxY = player.getY() + radiusY;

        double tolerance = 0.01;

        for (Entity entity : player.world.getEntitiesByClass(EndCrystalEntity.class, player.getBoundingBox().expand(radiusX, radiusY, radiusZ), endCrystal -> endCrystal.getY() >= minY && endCrystal.getY() <= maxY)) {
            double heightDifferenceAbove = player.getY() - entity.getY();
            double heightDifferenceBelow = entity.getY() - player.getY();

            if ((heightDifferenceAbove >= 0 && heightDifferenceAbove <= radiusY + tolerance) || (heightDifferenceBelow >= 0 && heightDifferenceBelow <= radiusBelowY + tolerance)) {
                if (mc.player == null || mc.world == null || mc.isPaused()) {
                    return;
                }

                for (int i = 0; i < 9; i++) {
                    Item item = mc.player.getInventory().getStack(i).getItem();
                    if (item == Items.TOTEM_OF_UNDYING) {
                        if (timerUtil.hasReached((int) cooldown.getValue())) {
                            mc.player.getInventory().selectedSlot = i;
                            timerUtil.reset();
                        }
                        break;
                    }
                }
            }
        }
    }

    private void anchorCheck(ClientPlayerEntity player) {
        double radiusX = anchorRadiusX.getValue();
        double radiusY = anchorRadiusYPlus.getValue();
        double radiusZ = anchorRadiusZ.getValue();
        double radiusBelowY = anchorRadiusYMinus.getValue();

        double minY = player.getY() - radiusY;
        double maxY = player.getY() + radiusY;

        double tolerance = 0.01;

        BlockPos.Mutable mutablePos = new BlockPos.Mutable();
        for (int x = (int) (player.getX() - radiusX); x <= player.getX() + radiusX; x++) {
            for (int y = (int) (player.getY() - radiusY); y <= player.getY() + radiusY; y++) {
                for (int z = (int) (player.getZ() - radiusZ); z <= player.getZ() + radiusZ; z++) {
                    mutablePos.set(x, y, z);
                    BlockState blockState = player.world.getBlockState(mutablePos);
                    if (blockState.getBlock() == Blocks.RESPAWN_ANCHOR) {
                        if (onlyCharged.isEnabled() && blockState.get(RespawnAnchorBlock.CHARGES) == 0) {
                            continue;
                        }

                        Optional<BlockPos> obsidianPosOpt = Optional.empty();
                        if (obsidianAnchorCheck.isEnabled()) {
                            obsidianPosOpt = getObsidianBlockingAnchor(player, mutablePos);
                        }

                        if (obsidianPosOpt.isPresent()) {
                            BlockPos obsidianPos = obsidianPosOpt.get();

                            // Get the vector pointing from the anchor to the player
                            double dx = player.getX() - mutablePos.getX();
                            double dy = player.getY() - mutablePos.getY();
                            double dz = player.getZ() - mutablePos.getZ();

                            // Get the vector pointing from the anchor to the obsidian
                            double ox = obsidianPos.getX() - mutablePos.getX();
                            double oy = obsidianPos.getY() - mutablePos.getY();
                            double oz = obsidianPos.getZ() - mutablePos.getZ();

                            // Check if the player is behind the obsidian block
                            if (dx * ox + dy * oy + dz * oz > 0) {
                                continue;
                            }
                        }


                        double heightDifferenceAbove = player.getY() - y;
                        double heightDifferenceBelow = y - player.getY();

                        if ((heightDifferenceAbove >= 0 && heightDifferenceAbove <= radiusY + tolerance) || (heightDifferenceBelow >= 0 && heightDifferenceBelow <= radiusBelowY + tolerance)) {
                            if (mc.player == null || mc.world == null || mc.isPaused()) {
                                return;
                            }

                            for (int i = 0; i < 9; i++) {
                                Item item = mc.player.getInventory().getStack(i).getItem();
                                if (item == Items.TOTEM_OF_UNDYING) {
                                    if (timerUtil.hasReached((int) cooldown.getValue())) {
                                        mc.player.getInventory().selectedSlot = i;
                                        timerUtil.reset();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkHealth(ClientPlayerEntity player) {
        float health = player.getHealth();
        int threshold = (int) healthIndicator.getValue();
        if (health <= threshold) {
            for (int i = 0; i < 9; i++) {
                Item item = mc.player.getInventory().getStack(i).getItem();
                if (item == Items.TOTEM_OF_UNDYING) {
                    if (timerUtil.hasReached((int) cooldown.getValue())) {
                        mc.player.getInventory().selectedSlot = i;
                        timerUtil.reset();
                    }
                    break;
                }
            }
        }
    }

    private void antiFall(ClientPlayerEntity player) {
        float health = player.getHealth();

        if (antiFall.isEnabled()) {
            if (!player.isOnGround()) {
                double distanceFallen = player.fallDistance;
                float fallDamage = (float) (distanceFallen - 3.0);
                if (fallDamage >= health) {
                    for (int i = 0; i < 9; i++) {
                        Item item = mc.player.getInventory().getStack(i).getItem();
                        if (item == Items.TOTEM_OF_UNDYING) {
                            if (timerUtil.hasReached((int) cooldown.getValue())) {
                                mc.player.getInventory().selectedSlot = i;
                                timerUtil.reset();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void handleOffhandPop(PlayerEntity player) {
        ItemStack offhandStack = player.getOffHandStack();
        if (previousOffhandItem == Items.TOTEM_OF_UNDYING && offhandStack.isEmpty()) {
            int totemSlot = findTotemInHotbar(player);
            if (totemSlot != -1) {
                player.getInventory().selectedSlot = totemSlot;
            }
        }
        // Update the previous offhand item
        previousOffhandItem = offhandStack.getItem();
    }

    private int findTotemInHotbar(PlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                return i;
            }
        }
        return -1;
    }

    public void predictDamage(PlayerEntity player, EnderPearlEntity enderPearl) { /* yeah its not finished yet :( */
        double throwHeight = player.getY();
        double predictedLandHeight = throwHeight - enderPearl.getVelocity().y;

        double fallDistance = throwHeight - predictedLandHeight;
        double damage;

        if (enderPearlPredict.isEnabled()) {
            if (fallDistance > 5.0) {
                damage = fallDistance - 3.0;
            } else {
                damage = 0;
            }

            sendMsg("Predicted Damage: " + damage);
        }
    }
}

