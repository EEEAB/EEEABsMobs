package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemSoulSummoningNecklace extends Item implements ICuriosItem {
    public ItemSoulSummoningNecklace(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        EMConfigHandler.Item item = EMConfigHandler.COMMON.ITEM;
        if (EMTUtils.SHOW_ITEM_CD) tooltip.add(EMTUtils.itemCoolTime(item.SSNCoolingTime.get()));
        tooltip.add(EMTUtils.simpleItemText(this.getDescriptionId(), item.SSNCumulativeMaximumDamage.get()));
    }
}
