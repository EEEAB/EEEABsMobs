package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemSoulSummonNecklace extends Item implements ICuriosItem {
    public ItemSoulSummonNecklace(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        ModConfigHandler.Item item = ModConfigHandler.COMMON.items;
        if (TranslateUtils.SHOW_ITEM_CD) tooltip.add(TranslateUtils.itemCoolTime(item.summoningSoulNecklaceConfig2.get()));
        tooltip.add(TranslateUtils.simpleItemText(this.getDescriptionId(), item.summoningSoulNecklaceConfig1.get()));
    }
}
