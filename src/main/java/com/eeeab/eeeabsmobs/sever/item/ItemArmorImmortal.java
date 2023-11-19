package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemArmorImmortal extends ArmorItem {
    public ItemArmorImmortal(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }


    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(Component.translatable("full_suit_of_armor_tip").setStyle(ItemInit.TIPS_GREEN));
        tooltip.add(Component.translatable("immortal_full_suit_of_armor_tip").setStyle(ItemInit.TIPS_GREEN));
        tooltip.add(Component.translatable("item.unable_depleted_tip").setStyle(ItemInit.TIPS_GRAY));
    }
}
