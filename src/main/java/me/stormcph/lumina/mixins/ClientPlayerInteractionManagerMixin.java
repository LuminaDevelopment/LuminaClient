package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.PlayerInteractItemEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "interactItem", at=@At("HEAD"), cancellable = true)
    public void interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        PlayerInteractItemEvent event = new PlayerInteractItemEvent(player, hand);
        event.call();
        if (event.isCancelled()) {
            cir.cancel();
        }
    }
}
