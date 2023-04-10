package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.PlayerMoveEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void onMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {

        if(!(((Object) this) instanceof ClientPlayerEntity)) return;
        if(!(((Object) this) == MinecraftClient.getInstance().player)) return;

        PlayerMoveEvent event = new PlayerMoveEvent(movementType, movement);
        event.call();
        // i am so fucking stupid i forgor to call the evenmt gnj ghdfisajoxiopdshjok
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
