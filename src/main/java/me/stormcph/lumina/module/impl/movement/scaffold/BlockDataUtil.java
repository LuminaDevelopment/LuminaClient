package me.stormcph.lumina.module.impl.movement.scaffold;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class BlockDataUtil {

    static MinecraftClient mc = MinecraftClient.getInstance();

    public static Vec3d getBlockSide(BlockPos pos, Direction face) {
        if (face == Direction.NORTH) {
            return new Vec3d(pos.getX(), pos.getY(), (double) pos.getZ() - 0.5);
        }
        if (face == Direction.EAST) {
            return new Vec3d((double) pos.getX() + 0.5, pos.getY(), pos.getZ());
        }
        if (face == Direction.SOUTH) {
            return new Vec3d(pos.getX(), pos.getY(), (double) pos.getZ() + 0.5);
        }
        if (face == Direction.WEST) {
            return new Vec3d((double) pos.getX() - 0.5, pos.getY(), pos.getZ());
        }
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockData getBlockData(BlockPos pos, List<Block> blacklistedBlocks, BlockData blockData) {
        if (!blacklistedBlocks.contains(mc.world.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), Direction.UP, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), mc.options.sprintKey.isPressed() && mc.player.isOnGround()
                    && mc.player.fallDistance == 0.0f
                    && mc.world
                    .getBlockState(new BlockPos(mc.player.getBlockX(), mc.player.getBlockY() - 1, mc.player.getBlockZ()))
                    .getBlock() == Blocks.AIR ? Direction.DOWN : Direction.EAST,
                    blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), mc.options.sprintKey.isPressed() && mc.player.isOnGround()
                    && mc.player.fallDistance == 0.0f
                    && mc.world
                    .getBlockState(new BlockPos(mc.player.getBlockX(), mc.player.getBlockY() - 1, mc.player.getBlockZ()))
                    .getBlock() == Blocks.AIR ? Direction.DOWN : Direction.WEST,
                    blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), mc.options.sprintKey.isPressed() && mc.player.isOnGround()
                    && mc.player.fallDistance == 0.0f
                    && mc.world
                    .getBlockState(new BlockPos(mc.player.getBlockX(), mc.player.getBlockY() - 1, mc.player.getBlockZ()))
                    .getBlock() == Blocks.AIR ? Direction.DOWN : Direction.SOUTH,
                    blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), mc.options.sprintKey.isPressed() && mc.player.isOnGround()
                    && mc.player.fallDistance == 0.0f
                    && mc.world
                    .getBlockState(new BlockPos(mc.player.getBlockX(), mc.player.getBlockY() - 1, mc.player.getBlockZ()))
                    .getBlock() == Blocks.AIR ? Direction.DOWN : Direction.NORTH,
                    blockData);
        }
        BlockPos add = pos.add(-1, 0, 0);
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), Direction.EAST, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), Direction.WEST, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), Direction.SOUTH, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), Direction.NORTH, blockData);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), Direction.EAST, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), Direction.WEST, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), Direction.SOUTH, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), Direction.NORTH, blockData);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), Direction.EAST, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), Direction.WEST, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), Direction.SOUTH, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), Direction.NORTH, blockData);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), Direction.EAST, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), Direction.WEST, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), Direction.SOUTH, blockData);
        }
        if (!blacklistedBlocks.contains(mc.world.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), Direction.NORTH, blockData);
        }
        return null;
    }
}
