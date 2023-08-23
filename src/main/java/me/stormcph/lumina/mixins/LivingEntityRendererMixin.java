package me.stormcph.lumina.mixins;

import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.misc.Freecam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
    @Shadow protected M model;
    @Shadow protected abstract float getHandSwingProgress(T entity, float tickDelta);
    @Shadow protected abstract float getAnimationProgress(T entity, float tickDelta);
    @Shadow protected abstract void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta);
    @Shadow protected abstract void scale(T entity, MatrixStack matrices, float amount);
    @Shadow
    public static int getOverlay(LivingEntity entity, float whiteOverlayProgress) { return 0; }
    @Shadow @Nullable protected abstract RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);
    @Shadow protected abstract float getAnimationCounter(T entity, float tickDelta);
    @Shadow @Final protected List<FeatureRenderer<T, M>> features;
    @Shadow protected abstract boolean isVisible(T entity);
    @Shadow
    public static boolean shouldFlipUpsideDown(LivingEntity entity) { return false; }

    public LivingEntityRendererMixin(EntityRendererFactory.Context ctx, M model, float shadowRadius) { super(ctx); }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    public void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (livingEntity.getUuid() == mc.player.getUuid() && ModuleManager.INSTANCE.getModuleByClass(Freecam.class).isEnabled() && ((Freecam) ModuleManager.INSTANCE.getModuleByClass(Freecam.class)).getRotationMode() == 2) {
            ci.cancel();
            Vec3d viewPos = mc.player.getEyePos();
            Vec3d targetPos = mc.crosshairTarget.getPos();
            Vec3d difPos = new Vec3d(targetPos.x - viewPos.x, targetPos.y - viewPos.y, Math.abs(targetPos.z - viewPos.z));
            float newPitch = (float) -(Math.atan2(difPos.y, Math.sqrt(Math.pow(difPos.x, 2) + Math.pow(difPos.z, 2))) * 180/Math.PI);

            matrixStack.push();
            this.model.handSwingProgress = this.getHandSwingProgress(livingEntity, g);
            this.model.riding = livingEntity.hasVehicle();
            this.model.child = livingEntity.isBaby();
            float h = MathHelper.lerpAngleDegrees(g, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
            float j = MathHelper.lerpAngleDegrees(g, livingEntity.prevHeadYaw, livingEntity.headYaw);
            float k = j - h;
            float l;
            if (livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity)livingEntity.getVehicle();
                h = MathHelper.lerpAngleDegrees(g, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
                k = j - h;
                l = MathHelper.wrapDegrees(k);
                if (l < -85.0F) {
                    l = -85.0F;
                }

                if (l >= 85.0F) {
                    l = 85.0F;
                }

                h = j - l;
                if (l * l > 2500.0F) {
                    h += l * 0.2F;
                }

                k = j - h;
            }

            float m = newPitch;
            if (shouldFlipUpsideDown(livingEntity)) {
                m *= -1.0F;
                k *= -1.0F;
            }

            float n;
            if (livingEntity.isInPose(EntityPose.SLEEPING)) {
                Direction direction = livingEntity.getSleepingDirection();
                if (direction != null) {
                    n = livingEntity.getEyeHeight(EntityPose.STANDING) - 0.1F;
                    matrixStack.translate((float)(-direction.getOffsetX()) * n, 0.0F, (float)(-direction.getOffsetZ()) * n);
                }
            }

            l = this.getAnimationProgress(livingEntity, g);
            this.setupTransforms(livingEntity, matrixStack, l, h, g);
            matrixStack.scale(-1.0F, -1.0F, 1.0F);
            this.scale(livingEntity, matrixStack, g);
            matrixStack.translate(0.0F, -1.501F, 0.0F);
            n = 0.0F;
            float o = 0.0F;
            if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
                n = livingEntity.limbAnimator.getSpeed(g);
                o = livingEntity.limbAnimator.getPos(g);
                if (livingEntity.isBaby()) {
                    o *= 3.0F;
                }

                if (n > 1.0F) {
                    n = 1.0F;
                }
            }

            this.model.animateModel(livingEntity, o, n, g);
            this.model.setAngles(livingEntity, o, n, l, k, m);
            boolean bl = this.isVisible(livingEntity);
            boolean bl2 = !bl && !livingEntity.isInvisibleTo(mc.player);
            boolean bl3 = mc.hasOutline(livingEntity);
            RenderLayer renderLayer = this.getRenderLayer(livingEntity, bl, bl2, bl3);
            if (renderLayer != null) {
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
                int p = getOverlay(livingEntity, this.getAnimationCounter(livingEntity, g));
                this.model.render(matrixStack, vertexConsumer, i, p, 1.0F, 1.0F, 1.0F, bl2 ? 0.15F : 1.0F);
            }

            if (!livingEntity.isSpectator()) {
                Iterator var23 = this.features.iterator();

                while(var23.hasNext()) {
                    FeatureRenderer<T, M> featureRenderer = (FeatureRenderer)var23.next();
                    featureRenderer.render(matrixStack, vertexConsumerProvider, i, livingEntity, o, n, g, l, k, m);
                }
            }

            matrixStack.pop();
            super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
        }
    }
}