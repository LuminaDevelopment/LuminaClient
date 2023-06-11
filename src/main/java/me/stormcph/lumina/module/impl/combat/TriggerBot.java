package me.stormcph.lumina.module.impl.combat;


// TODO: 15/05/2023 (Qweru) maybe in the future im lazy -> Hades: i started it (works basically)

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.time.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class TriggerBot extends Module {

    ModeSetting targets = new ModeSetting("Target", "Players", "Players", "Hostiles", "All", "Only Crystals");
    NumberSetting cooldownMS = new NumberSetting("Cooldown-ms", 0, 5000, 300, 10);
    private TimerUtil timer;

    public TriggerBot() {
         super("TriggerBot", "Automatically hits entities if looked at (blatant for now)", Category.COMBAT);
         addSettings(targets);
    }

    @Override
    public void onEnable() {
        timer = new TimerUtil();
        super.onEnable();
    }

    @EventTarget
    public void onEventUpdate(EventUpdate e) {
        if(nullCheck()) return;
        HitResult target = mc.crosshairTarget;

        if(targets.getMode().equalsIgnoreCase("Only Crystals")) {
            if (target.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHR = (EntityHitResult) target;
                Entity entity = entityHR.getEntity();
                mc.interactionManager.attackEntity(mc.player, entity);
                mc.player.swingHand(mc.player.getActiveHand());
            }
            return;
        }

        if (target != null) {
            if(timer.hasReached(Killaura.getTime())) {
                if (target.getType() == HitResult.Type.ENTITY) {
                    EntityHitResult entityHR = (EntityHitResult) target;
                    Entity entity = entityHR.getEntity();
                    if (!isValid(entity)) return;
                    mc.interactionManager.attackEntity(mc.player, entity);
                    mc.player.swingHand(mc.player.getActiveHand());
                }
                timer.reset();
            }
        }
        else timer.reset();
    }

    /*private Entity raycastEntity(double range) {
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
        Vec3d viewVector = mc.player.getRotationVecClient();
        Vec3d extendedPoint = cameraPos.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity && targets.getMode().equals("Players")) {
                if (entity.getBoundingBox().expand(0.3).intersects(cameraPos, extendedPoint)) {
                    return entity;
                }
            } else if(targets.getMode().equals("Hostiles")){

            }
        }

        return null;
    }*/

    private boolean isValid(Entity entity) {
        String mode = targets.getMode();
        return (
                (mode.equalsIgnoreCase("players") && entity instanceof PlayerEntity)
                        || (mode.equals("Hostiles") && entity instanceof HostileEntity)
                        || mode.equalsIgnoreCase("All")
                        || (mode.equalsIgnoreCase("Only Crystals") && entity instanceof EndCrystalEntity))
                && !(entity instanceof ProjectileEntity)
                && entity.isAlive();
    }
}
