package me.stormcph.lumina.mixins;

import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Session.class)
public interface ISession {
    @Accessor("username")
    void lumina$setUsername(String username);

    @Accessor("uuid")
    void lumina$setUuid(String uuid);
}
