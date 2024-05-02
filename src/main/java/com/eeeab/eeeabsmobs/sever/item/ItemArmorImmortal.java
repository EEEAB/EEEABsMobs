package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.item.util.EMArmorMaterial;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemArmorImmortal extends ArmorItem {
    public ItemArmorImmortal(EquipmentSlot type, Item.Properties properties) {
        super(EMArmorMaterial.IMMORTAL_MATERIAL, type, properties);
    }

    @Override
    public boolean canBeDepleted() {
        return EMConfigHandler.COMMON.ITEM.enableImmortalArmorItemDurability.get() && super.canBeDepleted();
    }


    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        if (!EMConfigHandler.COMMON.ITEM.enableImmortalArmorItemDurability.get())tooltip.add(EMTUtils.UNABLE_BREAKS);
        List<Component> componentList = EMTUtils.complexText(EMTUtils.ARMOR_PREFIX, false, EMTUtils.STYLE_GREEN,
                "full_suit_of_armor", "immortal_full_suit_of_armor");
        tooltip.addAll(componentList);
    }
}
