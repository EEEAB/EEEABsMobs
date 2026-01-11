package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
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

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.MUSIC_DISCS).add(
                ItemInit.GUARDIANS_MUSIC_DISC.get(),
                ItemInit.THE_ARMY_OF_MINOTAUR_MUSIC_DISC.get()
        );
        tag(ItemTags.LOGS_THAT_BURN).add(
                BlockInit.BLIGHTED_OAK_LOG.get().asItem(),
                BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get().asItem(),
                BlockInit.STRIPPED_BLIGHTED_OAK_WOOD.get().asItem()
        );
        tag(ItemTags.STONE_CRAFTING_MATERIALS).add(
                BlockInit.BLIGHTED_COBBLESTONE.get().asItem(),
                BlockInit.DARK_EROSION_ROCK.get().asItem()
        );
        tag(ItemTags.STONE_TOOL_MATERIALS).add(
                BlockInit.BLIGHTED_COBBLESTONE.get().asItem(),
                BlockInit.DARK_EROSION_ROCK.get().asItem()
        );
    }
}