package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.item.util.EEArmorMaterial;
import com.eeeab.eeeabsmobs.sever.util.MTUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;


public class ItemArmorImmortal extends ArmorItem {
    public ItemArmorImmortal(Type type) {
        super(EEArmorMaterial.IMMORTAL_MATERIAL, type, new Item.Properties());
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }


    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(MTUtils.UNABLE_BREAKS);
        List<Component> componentList = MTUtils.complexText(MTUtils.ARMOR_PREFIX, false, MTUtils.STYLE_GREEN,
                "full_suit_of_armor", "immortal_full_suit_of_armor");
        tooltip.addAll(componentList);
    }
}
