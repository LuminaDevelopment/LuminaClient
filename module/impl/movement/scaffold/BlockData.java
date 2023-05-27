package me.stormcph.lumina.module.impl.movement.scaffold;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockData {

    public Object pos;
    public static BlockPos position;
    public static Direction face;

    public BlockData(BlockPos position, Direction face, BlockData blockData) {
        BlockData.position = position;
        BlockData.face = face;
    }

    private Direction getFacing() {
        return face;
    }

    static Direction getFace(BlockData var0) {
        return var0.getFacing();
    }

    static BlockPos getPosition() {
        return position;
    }

    static Direction getFace() {
        return face;
    }
}
