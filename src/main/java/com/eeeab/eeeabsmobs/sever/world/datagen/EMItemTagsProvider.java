package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMItemTagsProvider extends ItemTagsProvider {
    public EMItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.MUSIC_DISCS)
                .add(ItemInit.GUARDIANS_MUSIC_DISC.get())
                .add(ItemInit.THE_ARMY_OF_MINOTAUR_MUSIC_DISC.get());
        tag(EMTagKey.EROSION_IMMUNE_ITEMS).add(Items.ELYTRA);
        tag(EMTagKey.DEMOLISHER_SUPPORTED_PROJECTILES)
                .addTag(Tags.Items.STONE)
                .add(Items.COBBLESTONE)
                .add(Items.MOSSY_COBBLESTONE)
                .add(Items.COBBLED_DEEPSLATE)
                .add(Items.BLACKSTONE)
                .remove(Items.INFESTED_STONE);
    }
}