package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.Cape;
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
        if(ModuleManager.INSTANCE.getModuleByName("Cape") != null && ModuleManager.INSTANCE.getModuleByName("Cape").isEnabled())
            info.setReturnValue(
                    new Identifier(
                            "lumina",
                            "textures/cape/"+Cape.getCurrentCape()+".png"
                            //"textures/cape/lumina-w.png"
                    )
            );
    }
}
