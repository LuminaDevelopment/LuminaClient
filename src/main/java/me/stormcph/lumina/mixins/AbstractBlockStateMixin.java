package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.FullBright;
import me.stormcph.lumina.module.impl.render.XRay;
import me.stormcph.lumina.setting.impl.NumberSetting;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow public abstract Block getBlock();
    @Inject(at = @At("RETURN"), method = "getRenderType", cancellable = true)
    public void getRenderType(CallbackInfoReturnable<BlockRenderType> cir) {
        if (ModuleManager.INSTANCE.getModuleByClass(XRay.class).isEnabled() && !XRay.visibleBlocksList.contains(this.getBlock().getTranslationKey())) {
            cir.setReturnValue(BlockRenderType.INVISIBLE);
        }
    }

    @Inject(at = @At("RETURN"), method = "getLuminance", cancellable = true)
    public void getLuminance(CallbackInfoReturnable ci){
        if(ModuleManager.INSTANCE.getModuleByClass(XRay.class).isEnabled()) ci.setReturnValue(15);
        if (ModuleManager.INSTANCE.getModuleByClass(FullBright.class).isEnabled()) ci.setReturnValue(((FullBright) ModuleManager.INSTANCE.getModuleByClass(FullBright.class)).getBrightness());
    }
}