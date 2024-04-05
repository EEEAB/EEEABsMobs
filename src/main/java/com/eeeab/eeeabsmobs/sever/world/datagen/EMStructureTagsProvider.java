package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.EMResourceKey;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMStructureTagsProvider extends StructureTagsProvider {
    public EMStructureTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EMTagKey.EYE_OF_GULING).add(EMResourceKey.GULING);
        this.tag(EMTagKey.EYE_OF_BLOODY_ALTAR).add(EMResourceKey.BLOODY_ALTAR);
    }
}
