package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FullBright extends Module {
    public FullBright() {
        super("Fullbright", "no more shadows!", Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player != null) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false));
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }
}

//if we somehow find a way to use gamma
/*package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;

public class FullBright extends Module {
        public FullBright() {
            super("Fullbright", "no more shadows!", Category.RENDER);
        }

        boolean isExisting = false;

        @Override
        public void onEnable() {
            super.onEnable();
            if (mc.player != null && !isExisting) {
                isExisting = true;
                mc.options.getGamma().setValue(10000.0);
            }
        }

        @Override
        public void onDisable() {
            super.onDisable();
            mc.options.getGamma().setValue(0.0);
        }
    }*/