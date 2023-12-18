package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.util.ModCreativeModeTabGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemImmortalDebris extends Item {
    public ItemImmortalDebris() {
        super(new Properties().tab(ModCreativeModeTabGroup.TABS));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, p_41422_, tooltip, flag);
        tooltip.add(Component.translatable(getDescriptionId() + ".tip").setStyle(ItemInit.TIPS_GRAY));
    }
}
