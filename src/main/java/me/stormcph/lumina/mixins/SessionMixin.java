package me.stormcph.lumina.mixins;

import me.stormcph.lumina.mixinterface.ISession;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Session.class)
public class SessionMixin implements ISession {

    @Shadow @Final @Mutable private String username;
    @Shadow @Final @Mutable private String uuid;

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}