package me.stormcph.lumina.module.impl.player;

import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {

    public AutoArmor() {
        super("AutoArmor", "", Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player == null) {
            return;
        }
        autoArmor();
    }

    public void autoArmor() {
        ItemStack[] currentArmor = mc.player.getInventory().armor.toArray(new ItemStack[0]);
        ItemStack[] newArmor = currentArmor.clone();

        for (ItemStack item : mc.player.getInventory().main) {
            if (item.getItem() instanceof ArmorItem armorItem) {
                int score = score(item);
                if (armorItem.getType() == ArmorItem.Type.HELMET) {
                    if (score > score(currentArmor[3])) {
                        newArmor[3] = item;
                    }
                }
                if (armorItem.getType() == ArmorItem.Type.CHESTPLATE) {
                    if (score > score(currentArmor[2])) {
                        newArmor[2] = item;
                    }
                }
                if (armorItem.getType() == ArmorItem.Type.LEGGINGS) {
                    if (score > score(currentArmor[1])) {
                        newArmor[1] = item;
                    }
                }
                if (armorItem.getType() == ArmorItem.Type.BOOTS) {
                    if (score > score(currentArmor[0])) {
                        newArmor[0] = item;
                    }
                }
            }
        }

        ItemStack helmet = newArmor[0];
        ItemStack chestplate = newArmor[1];
        ItemStack leggings = newArmor[2];
        ItemStack boots = newArmor[3];

        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, EquipmentSlot.HEAD.getEntitySlotId(), 0, SlotActionType.THROW, mc.player);
        mc.player.equipStack(EquipmentSlot.HEAD, helmet);

        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, EquipmentSlot.CHEST.getEntitySlotId(), 0, SlotActionType.THROW, mc.player);
        mc.player.equipStack(EquipmentSlot.CHEST, chestplate);

        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, EquipmentSlot.LEGS.getEntitySlotId(), 0, SlotActionType.THROW, mc.player);
        mc.player.equipStack(EquipmentSlot.LEGS, leggings);

        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, EquipmentSlot.FEET.getEntitySlotId(), 0, SlotActionType.THROW, mc.player);
        mc.player.equipStack(EquipmentSlot.FEET, boots);
    }

    public int score(ItemStack item) {
        int score = 0;
        Item itemObj = item.getItem();
        if (itemObj instanceof ArmorItem armorItem) {
            if (armorItem.getMaterial() == ArmorMaterials.LEATHER) {
                score = 1;
            } else if (armorItem.getMaterial() == ArmorMaterials.GOLD) {
                score = 2;
            } else if (armorItem.getMaterial() == ArmorMaterials.CHAIN) {
                score = 3;
            } else if (armorItem.getMaterial() == ArmorMaterials.IRON) {
                score = 4;
            } else if (armorItem.getMaterial() == ArmorMaterials.DIAMOND) {
                score = 5;
            } else if (armorItem.getMaterial() == ArmorMaterials.NETHERITE) {
                score = 6;
            }
        }
        if (EnchantmentHelper.getLevel(Enchantments.PROTECTION, item) > 0) {
            score += EnchantmentHelper.getLevel(Enchantments.PROTECTION, item);
        }
        return score;
    }

}
