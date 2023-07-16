package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.HandSwingEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At("HEAD"), cancellable = true)
    public void swingHand(Hand hand, CallbackInfo ci) {
        HandSwingEvent event = new HandSwingEvent(hand);
        event.call();
        if(event.isCancelled()) ci.cancel();
    }
}
