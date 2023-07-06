package me.stormcph.lumina.mixins;

import me.stormcph.lumina.cape.CapeManager;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.Cape;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {

    @Inject(method = "getCapeTexture", at = @At("HEAD"), cancellable = true)
    private void onGetCapeTexture(CallbackInfoReturnable<Identifier> info) {

        AbstractClientPlayerEntity thisPlayer = ((AbstractClientPlayerEntity) (Object) this);

        if(ModuleManager.INSTANCE.getModuleByName("Cape") != null && ModuleManager.INSTANCE.getModuleByName("Cape").isEnabled()) {
            for (String player : CapeManager.players.keySet()) {
                if (player.equalsIgnoreCase(thisPlayer.getGameProfile().getName()) || thisPlayer == MinecraftClient.getInstance().player) {
                    try {
                        String path = "textures/cape/" + CapeManager.players.get(player).getFileName();
                        info.setReturnValue(new Identifier("lumina", path));
                    }
                    catch (NullPointerException ignored) {}
                }
            }
        }
    }
}
