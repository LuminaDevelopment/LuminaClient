package me.stormcph.lumina.module.impl.player;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class GhostPlace extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Grim");
    private final List<BlockPos> positions = new ArrayList<>();

    public GhostPlace() {
        super("Ghostplace", "Every block you place will be a ghost block", Category.PLAYER);
        addSettings(mode);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        positions.clear();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {

        if(mode.getMode().equalsIgnoreCase("Vanilla")) {
            return;
        }

        for (BlockPos position : positions) {
            mc.world.setBlockState(position, Blocks.BEDROCK.getDefaultState());
        }
    }

    @EventTarget
    public void onPacket(PacketSendEvent e) {
        if(e.getPacket() instanceof PlayerInteractBlockC2SPacket pib) {

            if(mode.getMode().equalsIgnoreCase("Vanilla")) {
                e.cancel();
            }
            else {
                BlockPos pos = pib.getBlockHitResult().getBlockPos();
                switch (pib.getBlockHitResult().getSide()) {
                    case UP -> pos = pos.up();
                    case DOWN -> pos = pos.down();
                    case NORTH -> pos = pos.north();
                    case SOUTH -> pos = pos.south();
                    case EAST -> pos = pos.east();
                    case WEST -> pos = pos.west();
                }

                positions.add(pos);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (BlockPos position : positions) {
            mc.world.setBlockState(position, Blocks.AIR.getDefaultState());
        }
        positions.clear();
    }
}
