package me.stormcph.lumina.module.impl.combat;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
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
        super("TriggerBot", "Automatically hits entities if looked at (blatant for now)", Category.GHOST);
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
        Entity target = getTarget();

        if(mc.currentScreen != null) return;

        if(target != null) {
            if(timer.hasReached(Killaura.getTime()) && isValid(target)) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(mc.player.getActiveHand());
                timer.reset();
            }
        }
    }

    private Entity getTarget() {
        HitResult hitResult = mc.crosshairTarget;

        if(hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            return entityHitResult.getEntity();
        }

        return null;
    }

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
