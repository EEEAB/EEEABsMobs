package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.util.MTUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.eeeab.eeeabsmobs.sever.util.MTUtil.ARMOR_PREFIX;
import static com.eeeab.eeeabsmobs.sever.util.MTUtil.UNABLE_BREAKS;

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
        tooltip.add(UNABLE_BREAKS);
        List<Component> componentList = MTUtil.complexText(ARMOR_PREFIX, false, MTUtil.STYLE_GREEN,
                "full_suit_of_armor", "immortal_full_suit_of_armor");
        tooltip.addAll(componentList);
    }
}
