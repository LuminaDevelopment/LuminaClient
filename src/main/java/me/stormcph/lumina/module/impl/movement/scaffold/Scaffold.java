package me.stormcph.lumina.module.impl.movement.scaffold;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.mixinterface.IPMC2SP;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.movement.Sprint;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {

    private final BooleanSetting headTurner = new BooleanSetting("HeadTurner", false);

    private final List<Block> blacklistedBlocks = Arrays.asList(Blocks.AIR, Blocks.WATER,
            Blocks.LAVA, Blocks.ENDER_CHEST, Blocks.ENCHANTING_TABLE, Blocks.STONE_BUTTON,
            Blocks.CRAFTING_TABLE, Blocks.BEACON);

    private BlockData blockData;

    public Scaffold() {
        super("Scaffold", /* TODO */"", Category.MOVEMENT);
        addSettings(headTurner);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if(ModuleManager.INSTANCE.getModuleByClass(Sprint.class).isEnabled()) {
            sendMsg("&7Sprint is enabled. This may flag on some anticheats.");
        }
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent e) {
        Packet<?> packet = e.getPacket();
        if(packet instanceof PlayerMoveC2SPacket psa) {
            onPre(psa);
            onPost(psa);
        }
    }

    private void onPre(PlayerMoveC2SPacket psa) {
        BlockState underBlock = mc.world.getBlockState(mc.player.getBlockPos().down(1));

        if(headTurner.isEnabled() && underBlock.getBlock() instanceof AirBlock) {
            ((IPMC2SP) psa).setYaw(mc.player.getYaw() + 180);
            ((IPMC2SP) psa).setPitch(80);
        }
        else if (this.blockData != null) {
            ((IPMC2SP) psa).setYaw(mc.player.getYaw() + 180);
            ((IPMC2SP) psa).setPitch(80);
        }

        BlockPos blockBelow = new BlockPos(mc.player.getBlockX(), mc.player.getBlockY() - 1, mc.player.getBlockZ());
        if (mc.player != null) {
            this.blockData = BlockDataUtil.getBlockData(blockBelow, blacklistedBlocks, blockData);
            if (this.blockData == null) {
                this.blockData = BlockDataUtil.getBlockData(blockBelow.offset(Direction.DOWN), blacklistedBlocks, blockData);
            }
        }
    }

    private void onPost(PlayerMoveC2SPacket psa) {
        BlockState underBlock = mc.world
                .getBlockState(new BlockPos(mc.player.getBlockX(), mc.player.getBlockY() - 1, mc.player.getBlockZ()));
        boolean flag = underBlock.getBlock().equals(Blocks.AIR) || !mc.player.isOnGround();
        if (flag) {
            place(mc.player.getMainHandStack(), BlockData.position, BlockData.face,
                    new Vec3d(BlockData.getPosition().getX(), BlockData.getPosition().getY(), BlockData.getPosition().getZ()).add(0.5, 0.5, 0.5).add(scale(
                            new Vec3d(BlockData.getFace(this.blockData).getVector().getX(),
                                    BlockData.getFace(this.blockData).getVector().getY(),
                                    BlockData.getFace(this.blockData).getVector().getZ()))));
            mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(mc.player.getActiveHand()));
        }
    }

    private Vec3d scale(Vec3d vec) {
        return new Vec3d(vec.getX() * 0.5, vec.getY() * 0.5, vec.getZ() * 0.5);
    }

    private void place(ItemStack heldStack, BlockPos hitPos, Direction side, Vec3d hitVec) {
        float f = (float) (hitVec.x - (double) hitPos.getX());
        float f1 = (float) (hitVec.y - (double) hitPos.getY());
        float f2 = (float) (hitVec.z - (double) hitPos.getZ());
        mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(mc.player.getActiveHand(), new BlockHitResult(hitVec, side, hitPos, false), 0));
    }
}
