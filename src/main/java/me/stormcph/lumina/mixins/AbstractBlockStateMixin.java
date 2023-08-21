package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.XRay;
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
}