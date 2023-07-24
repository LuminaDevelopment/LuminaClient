package me.stormcph.lumina.mixins;

import me.stormcph.lumina.event.impl.ItemSwitchAnimationEvent;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.Animations;
import me.stormcph.lumina.module.impl.render.ViewModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.render.item.HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    public void renderItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if(ModuleManager.INSTANCE.getModuleByClass(Animations.class).isEnabled()) {
            ((Animations) ModuleManager.INSTANCE.getModuleByClass(Animations.class)).render(item, matrices);
        }
        else if(ModuleManager.INSTANCE.getModuleByClass(ViewModel.class).isEnabled() && hand == Hand.MAIN_HAND) {
            ((ViewModel) ModuleManager.INSTANCE.getModuleByClass(ViewModel.class)).render(item, matrices);
        }
    }

    @Shadow
    private float equipProgressMainHand;
    @Shadow
    private float prevEquipProgressMainHand;
    @Shadow private float prevEquipProgressOffHand;
    @Shadow private float equipProgressOffHand;
    @Shadow private ItemStack mainHand;
    @Shadow private ItemStack offHand;

    @Final @Shadow private MinecraftClient client;


    @Inject(method = "updateHeldItems", at = @At("HEAD"), cancellable = true)
    public void updateHeldItems(CallbackInfo ci) {
        ItemSwitchAnimationEvent isa = new ItemSwitchAnimationEvent(equipProgressMainHand);
        isa.call();
        if(isa.cancelled) ci.cancel();

        if(client.player == null) return;
        if(ModuleManager.INSTANCE.getModuleByClass(Animations.class).isEnabled() && client.options.useKey.isPressed()) {

            ItemStack stack = client.player.getInventory().getMainHandStack();

            if(stack.getItem() instanceof SwordItem ||
                    (stack.getItem() instanceof AxeItem && (Animations.mode.getMode().equals("Weapons") || Animations.mode.getMode().equals("All"))) ||
                    (stack.getItem() instanceof ShovelItem && (Animations.mode.getMode().equals("All"))) ||
                    (stack.getItem() instanceof PickaxeItem && (Animations.mode.getMode().equals("All"))) ||
                    (stack.getItem() instanceof HoeItem && (Animations.mode.getMode().equals("All")))) {

                this.equipProgressMainHand = 1;
                this.equipProgressOffHand = 1;

                this.prevEquipProgressMainHand = this.equipProgressMainHand;
                this.prevEquipProgressOffHand = this.equipProgressOffHand;
                ClientPlayerEntity clientPlayerEntity = this.client.player;
                ItemStack itemStack = clientPlayerEntity.getMainHandStack();
                ItemStack itemStack2 = clientPlayerEntity.getOffHandStack();

                if (ItemStack.areEqual(this.mainHand, itemStack)) {
                    this.mainHand = itemStack;
                }
                if (ItemStack.areEqual(this.offHand, itemStack2)) {
                    this.offHand = itemStack2;
                }

                this.mainHand = itemStack;
                this.offHand = itemStack2;

                ci.cancel();
            }
        }
    }

    @ModifyVariable(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "STORE", ordinal = 0), index = 6)
    private float modifySwing(float value) {
        if(ModuleManager.INSTANCE.getModuleByClass(Animations.class).isEnabled() && client.options.useKey.isPressed()) {
            ItemStack stack = client.player.getInventory().getMainHandStack();
            if(stack.getItem() instanceof SwordItem ||
                    (stack.getItem() instanceof AxeItem && (Animations.mode.getMode().equals("Weapons") || Animations.mode.getMode().equals("All"))) ||
                    (stack.getItem() instanceof ShovelItem && (Animations.mode.getMode().equals("All"))) ||
                    (stack.getItem() instanceof PickaxeItem && (Animations.mode.getMode().equals("All"))) ||
                    (stack.getItem() instanceof HoeItem && (Animations.mode.getMode().equals("All")))) {
                return 0;
            }
        }

        return value;
    }
}
