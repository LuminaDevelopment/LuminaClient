package me.stormcph.lumina.module.impl.movement;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.player.PacketUtil;
import me.stormcph.lumina.utils.TimerUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class FakeLag extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", 0.0, 1000.0, 300.0, 1.0);

    private TimerUtil timer;
    private final List<Packet<?>> packets = new ArrayList<>();

    private boolean sending;
    // Todo: Render fake lag
    private Vec3d lastPos;

    public FakeLag() {
        super("FakeLag", "yes", Category.MOVEMENT);
        addSettings(delay);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        packets.clear();
        timer = new TimerUtil();
        lastPos = mc.player.getPos();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(nullCheck()) return;

        if(timer.hasReached(delay.getFloatValue())) {
            sending = true;
            timer.reset();

            if(packets.isEmpty()) {
                sending = false;
                return;
            }

            for (Packet<?> packet : packets) {
                PacketUtil.sendPacket(packet);
            }

            sending = false;
            packets.clear();
            lastPos = mc.player.getPos();
        }
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent e) {
        if(nullCheck()) return;

        if(!sending) {
            packets.add(e.getPacket());
            e.setCancelled(true);
        }
    }
}
