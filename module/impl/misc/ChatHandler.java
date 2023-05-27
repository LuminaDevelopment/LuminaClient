package me.stormcph.lumina.module.impl.misc;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.PacketSendEvent;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.impl.chat.ChatCommandManager;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

import java.util.List;

public class ChatHandler extends Module {

    private final String prefix = "$";

    public ChatHandler() {
        super("ChatCommands", "// TODO", Category.MISC);
        ChatCommandManager.init();
    }

    @EventTarget
    public void onPacketSend(PacketSendEvent e){
        if(nullCheck()) return;
        if(e.getPacket() instanceof ChatMessageC2SPacket cmp) {
            if(cmp.chatMessage().startsWith(prefix)){
                e.setCancelled(true);
                ChatCommandManager.findCommand(
                        cmp.chatMessage().replace(
                                prefix,
                                "").split(" ")[0]
                ).execute(
                        List.of(
                                cmp.chatMessage()
                                        .replace(
                                                prefix,
                                                "")
                                        .split(" ")
                        )
                );
            }
        }
    }

}
