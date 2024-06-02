package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.world.biome.BiomesCrackProvider;
import com.eeeab.eeeabsmobs.sever.world.dimension.DimensionsCrackProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EMWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, BiomesCrackProvider::boostrap)
            .add(Registries.DIMENSION_TYPE, DimensionsCrackProvider::bootstrapType)
            .add(Registries.LEVEL_STEM, DimensionsCrackProvider::bootstrapStem);

    public EMWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(EEEABMobs.MOD_ID));
    }
}