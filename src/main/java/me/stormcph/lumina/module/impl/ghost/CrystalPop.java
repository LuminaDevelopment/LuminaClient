package me.stormcph.lumina.module.impl.ghost;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.BooleanSetting;
import me.stormcph.lumina.setting.impl.NumberSetting;
import me.stormcph.lumina.utils.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class CrystalPop extends Module {

    private final TimerUtil timerUtil = new TimerUtil();

    private final NumberSetting cooldown = new NumberSetting("cooldown-ms", 0.0, 1000.0, 0.0, 0.01);
    //private final NumberSetting range = new NumberSetting("Range", 1.0, 10.0, 5.0, 0.1);

    private final BooleanSetting onlyOwnCrystal = new BooleanSetting("OnlyOwnCrystal", false);
    private final BooleanSetting tryPunch = new BooleanSetting("DonutSmpBypass", false);

    private boolean playerPlacedCrystal = false;

    public CrystalPop() {
        super("CrystalPop", "Automatically pops end crystal when placed", Category.GHOST);
        addSettings(cooldown, onlyOwnCrystal, /*range,*/ tryPunch);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        endCrystalTrigger();
        trackPlacedCrystals();
    }

    private void endCrystalTrigger() {
        Entity target = raycastEndCrystal(5);
        if (target == null)
            return;

        // Check if onlyOwnCrystal is enabled and the player hasn't placed a crystal yet
        if (onlyOwnCrystal.isEnabled() && !playerPlacedCrystal)
            return;

        if (timerUtil.hasReached((int) cooldown.getValue())) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
            timerUtil.reset();
            playerPlacedCrystal = false;
        }
    }

    private void trackPlacedCrystals() {
        if (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL && mc.options.useKey.isPressed()) {
            playerPlacedCrystal = true;
        }
    }

    private Entity raycastEndCrystal(double range) {
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
        Vec3d viewVector = mc.player.getRotationVecClient();
        Vec3d extendedPoint = cameraPos.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof EndCrystalEntity) {
                if (entity.getBoundingBox().expand(0.3).intersects(cameraPos, extendedPoint)) {
                    return entity;
                }
            }
        }

        return null;
    }
}
