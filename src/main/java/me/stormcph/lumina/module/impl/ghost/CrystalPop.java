package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class CrystalPop extends Module {

    private final TimerUtil timerUtil = new TimerUtil();

    private final NumberSetting cooldown = new NumberSetting("cooldown-ms", 0.0, 1000.0, 0.0, 0.01);

    BooleanSetting hitboxCheck = new BooleanSetting("hotboxCheck", false);
    public CrystalPop() {
        super("CrystalPop", "Automatically pops end crystal when placed", Category.GHOST);
        addSettings(cooldown, hitboxCheck);
    }

    @Override
    public void onEnable() {
        super.onEnable();
//        ClientTickEvents.END_CLIENT_TICK.register(client -> endCrystalTrigger());
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        endCrystalTrigger();
    }
    private void endCrystalTrigger() {
        HitResult hit = mc.crosshairTarget;
        if (hit.getType() != HitResult.Type.ENTITY)
            return;
        Entity target = ((EntityHitResult) hit).getEntity();
        if (!(target instanceof EndCrystalEntity))
            return;
        if (timerUtil.hasReached((int) cooldown.getValue())) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
            timerUtil.reset();
        }

    }

    public void hitboxCheck() {
        if (hitboxCheck.isEnabled()) {
            //hi chatgpt here is where you come into play :)
        }
    }
}