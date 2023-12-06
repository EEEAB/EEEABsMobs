package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

//自定义方块掉落
public class EEBlockLootProvider extends BlockLootSubProvider {
    protected EEBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
