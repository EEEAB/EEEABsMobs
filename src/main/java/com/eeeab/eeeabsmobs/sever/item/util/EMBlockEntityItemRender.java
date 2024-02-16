package com.eeeab.eeeabsmobs.sever.item.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class EMBlockEntityItemRender extends BlockItem {

    public EMBlockEntityItemRender(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) EEEABMobs.PROXY.getISTERProperties());
    }
}
