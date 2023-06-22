package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.AttackEntityEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class InteractionManagerMixin {

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        AttackEntityEvent event = new AttackEntityEvent(target);
        event.call();
        if(event.isCancelled()) ci.cancel();
    }
}
