package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.util.MTUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemImmortalDebris extends Item {
    public ItemImmortalDebris() {
        super(new Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, p_41422_, tooltip, flag);
        tooltip.add(MTUtils.simpleItemText("immortal_debris", MTUtils.STYLE_GRAY));
    }
}
