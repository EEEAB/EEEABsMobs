package com.eeeab.eeeabsmobs.sever.item.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class EEArmorUtil {
    private EEArmorUtil(){}

    //检查玩家是否穿戴全套盔甲
    public static boolean checkFullSuitOfArmor(ArmorMaterial material, Player player) {
        ItemStack boots = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack chestPlate = player.getInventory().getArmor(2);
        ItemStack helmet = player.getInventory().getArmor(3);
        if (boots.isEmpty() || leggings.isEmpty() || chestPlate.isEmpty() || helmet.isEmpty()) {
            return false;
        }

        //避免出现类型转换异常
        for (ItemStack itemStack : player.getInventory().armor) {
            if (!(itemStack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }

        ArmorItem bootsItem = (ArmorItem) boots.getItem();
        ArmorItem leggingsItem = (ArmorItem) leggings.getItem();
        ArmorItem chestPlateItem = (ArmorItem) chestPlate.getItem();
        ArmorItem helmetItem = (ArmorItem) helmet.getItem();

        return bootsItem.getMaterial() == material && leggingsItem.getMaterial() == material && chestPlateItem.getMaterial() == material && helmetItem.getMaterial() == material;
    }
}
