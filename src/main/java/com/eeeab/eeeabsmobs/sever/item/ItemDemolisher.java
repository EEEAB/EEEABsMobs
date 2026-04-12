package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;

import java.util.function.Consumer;

//TODO 保留至0.99版本后移除
@Deprecated
public class ItemDemolisher extends SwordItem implements IUnbreakableItem {
    public ItemDemolisher(Tier tier, Properties properties) {
        super(tier, 5, -2.4F, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) EEEABMobs.PROXY.getISTERProperties());
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.SWORD_DIG == toolAction;
    }

    @Override
    public boolean canBreakItem() {
        return false;
    }
}
