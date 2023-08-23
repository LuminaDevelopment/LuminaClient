package me.stormcph.lumina.module.impl.combat;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.Animations;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.player.PacketUtil;
import me.stormcph.lumina.utils.player.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Comparator;
import java.util.List;

public class Killaura extends Module {

    private final NumberSetting range = new NumberSetting("Range", 1.0, 6.0, 3.0, 0.1);
    private final ModeSetting priority = new ModeSetting("Distance", "Distance", "Health");
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", true);
    private final BooleanSetting passive = new BooleanSetting("Passive", true);

    public Killaura() {
        super("Killaura", "Attacks nearby enemies", Category.COMBAT);
        addSettings(range, players, mobs, passive, priority);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(nullCheck()) return;
        List<LivingEntity> targets = nearestTarget();
        if(targets.isEmpty()) return;
        LivingEntity target = targets.get(0);

        rotate(target);

        if(mc.player.getAttackCooldownProgress(0.5f) == 1) {
            mc.interactionManager.attackEntity(mc.player, target);
            if(mc.options.useKey.isPressed() && ModuleManager.INSTANCE.getModuleByClass(Animations.class).isEnabled()) {
                // Downcast to Animations module for swinging
                ((Animations) ModuleManager.INSTANCE.getModuleByClass(Animations.class)).swing();
                PacketUtil.sendPacket(new HandSwingC2SPacket(mc.player.getActiveHand()));
            }
            else mc.player.swingHand(mc.player.getActiveHand());
        }
    }

    private void rotate(Entity target) {
        float[] rotations = RotationUtil.getRotations(target);
        PacketUtil.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations[0], rotations[1], mc.player.isOnGround()));
        mc.player.bodyYaw = rotations[0];
        mc.player.headYaw = rotations[0];
    }

    private List<LivingEntity> nearestTarget() {

        Comparator<LivingEntity> comparator = priority.isMode("Distance") ? Comparator.comparingDouble(entity -> entity.distanceTo(mc.player)) : Comparator.comparingDouble(LivingEntity::getHealth);

        return mc.world.getEntitiesByClass(LivingEntity.class, mc.player.getBoundingBox().expand(range.getValue()), this::isValid)
                .stream().sorted(comparator).toList();
    }

    private boolean isValid(Entity entity) {
        if(entity == mc.player) return false;
        if(!entity.isAlive()) return false;
        else if(entity instanceof PlayerEntity && !players.isEnabled()) return false;
        else if(entity instanceof PassiveEntity && !passive.isEnabled()) return false;
        else if(entity instanceof HostileEntity && !mobs.isEnabled()) return false;
        else if (!(entity instanceof LivingEntity)) return false;
        else return true;
    }

}
