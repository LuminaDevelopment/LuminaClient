package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import java.util.Arrays;
import java.util.List;

public class XRay extends Module {
    public static List visibleBlocksList = Arrays.asList(new String[]{
            "block.minecraft.water",
            "block.minecraft.lava",
            "block.minecraft.coal_ore",
            "block.minecraft.copper_ore",
            "block.minecraft.iron_ore",
            "block.minecraft.gold_ore",
            "block.minecraft.diamond_ore",
            "block.minecraft.redstone_ore",
            "block.minecraft.lapis_ore",
            "block.minecraft.emerald_ore",
            "block.minecraft.deepslate_coal_ore",
            "block.minecraft.deepslate_copper_ore",
            "block.minecraft.deepslate_iron_ore",
            "block.minecraft.deepslate_gold_ore",
            "block.minecraft.deepslate_diamond_ore",
            "block.minecraft.deepslate_redstone_ore",
            "block.minecraft.deepslate_lapis_ore",
            "block.minecraft.deepslate_emerald_ore",
            "block.minecraft.nether_quartz_ore",
            "block.minecraft.nether_gold_ore",
            "block.minecraft.ancient_debris"
    });

    public XRay() {
        super("XRay", "See only ores and liquids", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        mc.worldRenderer.reload();
        super.onDisable();
    }

    @Override
    public void onDisable() {
        if(nullCheck()) return;
        mc.worldRenderer.reload();
        super.onDisable();
    }

    @EventTarget
    public void onTick(EventUpdate e) {
        if(nullCheck()) return;
        if (this.isEnabled()) mc.chunkCullingEnabled = false;
    }
}
