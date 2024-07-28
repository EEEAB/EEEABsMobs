package com.eeeab.eeeabsmobs.client.render.util;

import com.eeeab.eeeabsmobs.client.model.armor.ModelGhostWarriorArmor;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class EMArmorStackRenderProperties implements IClientItemExtensions {
    public static boolean status;
    public static ModelGhostWarriorArmor GHOST_WARRIOR_ARMOR;
    public static ModelGhostWarriorArmor GHOST_WARRIOR_ARMOR_LEGS;

    public static void init() {
        status = true;
        GHOST_WARRIOR_ARMOR = new ModelGhostWarriorArmor(Minecraft.getInstance().getEntityModels().bakeLayer(EMModelLayer.GHOST_WARRIOR_ARMOR));
        GHOST_WARRIOR_ARMOR_LEGS = new ModelGhostWarriorArmor(Minecraft.getInstance().getEntityModels().bakeLayer(EMModelLayer.GHOST_WARRIOR_ARMOR_LEGS));
    }

    public EMArmorStackRenderProperties() {
    }

    @Override
    public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        if (!status) {
            init();
        }
        if (itemStack.is(ItemInit.GHOST_WARRIOR_HELMET.get())) {
            return GHOST_WARRIOR_ARMOR;
        } else if (itemStack.is(ItemInit.GHOST_WARRIOR_CHESTPLATE.get())) {
            return GHOST_WARRIOR_ARMOR;
        } else if (itemStack.is(ItemInit.GHOST_WARRIOR_LEGGINGS.get())) {
            return GHOST_WARRIOR_ARMOR_LEGS;
        } else if (itemStack.is(ItemInit.GHOST_WARRIOR_BOOTS.get())) {
            return GHOST_WARRIOR_ARMOR;
        }
        return original;
    }
}
