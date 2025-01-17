package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EMTabGroup {

    public static CreativeModeTab TABS = new CreativeModeTab(EEEABMobs.MOD_ID + ".creative_tab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ItemInit.GUARDIAN_AXE.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            super.fillItemList(items);
        }
    };
}
