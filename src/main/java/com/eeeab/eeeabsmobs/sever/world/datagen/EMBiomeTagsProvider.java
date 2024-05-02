package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class EMBiomeTagsProvider extends BiomeTagsProvider {

    public EMBiomeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(EMTagKey.GULING_BIOMES).addTag(Tags.Biomes.IS_UNDERGROUND);
        tag(EMTagKey.BLOODY_ALTAR_BIOMES).addTag(BiomeTags.IS_NETHER);
    }
}
