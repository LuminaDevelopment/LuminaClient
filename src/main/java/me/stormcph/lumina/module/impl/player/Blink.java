package me.stormcph.lumina.module.impl.player;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.utils.player.PacketUtil;
import me.stormcph.lumina.utils.render.FakePlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Blink extends Module {

    private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    private FakePlayerEntity model;

    public Blink() {
        super("Blink", "Cancels all packets", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        model = new FakePlayerEntity(mc.player, mc.player.getGameProfile().getName(), 20, true);
        model.doNotPush = true;
        model.hideWhenInsideCamera = true;
        model.spawn();
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent e) {
        Packet<?> packet = e.getPacket();
        if (packet instanceof KeepAliveC2SPacket || packet instanceof QueryPingC2SPacket) {
            return;
        }
        else if(packet instanceof PlayerInteractBlockC2SPacket pib) {
            ItemStack item = mc.player.getStackInHand(pib.getHand());
            if(item != null) {
                int count = item.getCount();
                if (item.getItem() instanceof BlockItem) {
                    mc.player.getStackInHand(pib.getHand()).setCount(count + 1);
                }
            }
        }
        e.cancel();
        packets.add(packet);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (Packet<?> packet : packets) {
            PacketUtil.sendPacket(packet);
        }
        packets.clear();

        if (model != null) {
            model.despawn();
            model = null;
        }
    }
}
