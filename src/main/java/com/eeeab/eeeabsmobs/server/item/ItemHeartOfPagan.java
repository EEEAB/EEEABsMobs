package com.eeeab.eeeabsmobs.server.item;

import com.eeeab.eeeabsmobs.server.util.TranslateUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemHeartOfPagan extends Item {
    public ItemHeartOfPagan(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(TranslateUtils.simpleItemText(this.getDescriptionId()));
    }
}
