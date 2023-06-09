package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Shadow @Nullable private String splashText;

    @Mutable
    @Shadow @Final public static Text COPYRIGHT;

    @Inject(method = "tick", at=@At("HEAD"), cancellable = true)
    private void init(CallbackInfo ci){
        if(ModuleManager.INSTANCE.getModuleByName("TitleScreen").isEnabled()){
            this.splashText="Prototype!";
            this.COPYRIGHT = Text.of("LuminaClient");
        }
    }
}
