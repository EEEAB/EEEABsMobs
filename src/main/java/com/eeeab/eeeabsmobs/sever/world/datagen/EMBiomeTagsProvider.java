package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMBiomeTagsProvider extends BiomeTagsProvider {
    public EMBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EMTagKey.GULING_BIOMES).addTag(Tags.Biomes.IS_UNDERGROUND);
        tag(EMTagKey.BLOODY_ALTAR_BIOMES)
                .add(Biomes.CRIMSON_FOREST)
                .add(Biomes.NETHER_WASTES)
                .add(Biomes.SOUL_SAND_VALLEY)
                .add(Biomes.WARPED_FOREST);
    }
}
