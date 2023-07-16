package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.misc.NameProtect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    public void getPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {

        if(ModuleManager.INSTANCE.getModuleByClass(NameProtect.class).isEnabled()) {
            if (entry.getProfile().getName().equals(MinecraftClient.getInstance().getSession().getUsername())) {
                cir.setReturnValue(Text.of("LuminaUser"));
            }
        }
    }
}
