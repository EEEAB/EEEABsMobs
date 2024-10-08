package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class EMBlockTagsProvider extends BlockTagsProvider {
    public EMBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                BlockInit.IMMORTAL_BLOCK.get(),
                BlockInit.GHOST_STEEL_BLOCK.get(),
                BlockInit.TOMB_ARROWS_TRAP.get(),
                BlockInit.TOMB_GAS_TRAP.get(),
                BlockInit.TOMB_SUMMON_TRAP.get(),
                BlockInit.EROSION_ROCK_BRICKS.get());
        tag(BlockTags.MINEABLE_WITH_HOE).add(
                BlockInit.SOUL_LIGHT.get());
        tag(BlockTags.NEEDS_IRON_TOOL).add(
                BlockInit.IMMORTAL_BLOCK.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                BlockInit.GHOST_STEEL_BLOCK.get(),
                BlockInit.TOMB_ARROWS_TRAP.get(),
                BlockInit.TOMB_GAS_TRAP.get(),
                BlockInit.TOMB_SUMMON_TRAP.get());
    }
}