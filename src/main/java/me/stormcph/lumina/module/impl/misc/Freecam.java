package me.stormcph.lumina.module.impl.misc;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public class Freecam extends Module {
    public Vec3d pos;
    public float yaw;
    public float pitch;
    private GameMode oldGameMode = GameMode.SURVIVAL;
    public Freecam() {
        super("Freecam", "Leave your body and enter spectator mode", Category.MISC);
        noSave();
    }

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        super.onEnable();
        oldGameMode = mc.interactionManager.getCurrentGameMode();
        mc.interactionManager.setGameMode(GameMode.SPECTATOR);
        pos = mc.player.getPos();
        yaw = mc.player.getYaw();
        pitch = mc.player.getPitch();
    }

    @Override
    public void onDisable() {
        if(nullCheck()) return;
        super.onDisable();
        mc.interactionManager.setGameMode(oldGameMode);
        mc.player.setPos(pos.getX(), pos.getY(), pos.getZ());
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent e) {
        if (ModuleManager.INSTANCE.getModuleByName("Freecam").isEnabled() && e.getPacket() instanceof PlayerMoveC2SPacket) e.setCancelled(true);
    }
}
