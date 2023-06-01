package me.stormcph.lumina.module.impl.combat;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.module.ModuleManager;
import me.stormcph.lumina.module.impl.render.Animations;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.misc.PacketUtil;
import me.stormcph.lumina.utils.RotationUtil;
import me.stormcph.lumina.utils.time.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Killaura extends Module {

    private final NumberSetting range = new NumberSetting("Range", 1.0, 6.0, 3.0, 0.1);

    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", true);
    private final BooleanSetting passive = new BooleanSetting("Passive", true);
    private final BooleanSetting autoblock = new BooleanSetting("Autoblock", true);

    private TimerUtil timer;

    public Killaura() {
        super("Killaura", "Attacks nearby enemies", Category.COMBAT);
        addSettings(range, players, mobs, passive, autoblock);
    }

    @Override
    public void onEnable() {
        timer = new TimerUtil();
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if(nullCheck()) return;
        Entity target = nearestTarget();
        if(target == null) return;

        rotate(target);

        // Todo: Add autoblock

        if(timer.hasReached(getTime())) {
            mc.interactionManager.attackEntity(mc.player, target);
            if(mc.options.useKey.isPressed() && ModuleManager.INSTANCE.getModuleByClass(Animations.class).isEnabled()) {
                ((Animations) ModuleManager.INSTANCE.getModuleByClass(Animations.class)).swing();
                PacketUtil.sendPacket(new HandSwingC2SPacket(mc.player.getActiveHand()));
            }
            else mc.player.swingHand(mc.player.getActiveHand());
            timer.reset();
        }
    }

    private void rotate(Entity target) {
        float[] rotations = RotationUtil.getRotations(target);
        PacketUtil.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations[0], 5, mc.player.isOnGround()));
        // Todo: Add client side rotations
       // mc.player.updateTrackedHeadRotation(rotations[0], 10);
    }

    private Entity nearestTarget() {

        if(nullCheck()) return null;

        for(Entity entity : mc.world.getEntities()) {
            if(entity.distanceTo(mc.player) > range.getValue()) continue;
            if(!isValid(entity)) continue;
            return entity;
        }

        return null;
    }

    private boolean isValid(Entity entity) {
        if(entity == mc.player) return false;
        if(!entity.isAlive()) return false;
        else if(entity instanceof PlayerEntity && !players.isEnabled()) return false;
        else if(entity instanceof PassiveEntity && !passive.isEnabled()) return false;
        else if(entity instanceof HostileEntity && !mobs.isEnabled()) return false;
        else if(entity instanceof ItemEntity) return false;
        else if(entity instanceof ItemFrameEntity) return false;
        else if(entity instanceof ExperienceOrbEntity) return false;
        else if(entity instanceof ExperienceBottleEntity) return false;
        else if(entity instanceof ArrowEntity) return false;
        else if(entity instanceof TridentEntity) return false;
        else return true;
    }

    // Times can be changed here
    private long getTime() {
        Item item = mc.player.getInventory().getMainHandStack().getItem();
        if(item instanceof AirBlockItem) {
            return 400;
        }
        else if(item instanceof SwordItem) {
            return 500;
        }
        else if(item instanceof ShovelItem) {
            return 750;
        }
        else if(item instanceof PickaxeItem) {
            return 700;
        }
        else if(item instanceof AxeItem) {
            return 1000;
        }
        else if(item instanceof HoeItem) {
            return 750;
        }

        return 400;
    }
}
