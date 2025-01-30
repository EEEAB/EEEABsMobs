package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.world.datagen.damage.EMDamageSourceProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.place.EMConfiguredFeatures;
import com.eeeab.eeeabsmobs.sever.world.datagen.place.EMPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EMDatapackBuiltinProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, EMDamageSourceProvider::boostrap)
            .add(Registries.CONFIGURED_FEATURE, EMConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, EMPlacedFeatures::bootstrap);
            //.add(Registries.BIOME, BiomesProvider::boostrap)
            //.add(Registries.DIMENSION_TYPE, DimensionsProvider::bootstrapType)
            //.add(Registries.LEVEL_STEM, DimensionsProvider::bootstrapStem);

    public EMDatapackBuiltinProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(EEEABMobs.MOD_ID));
    }
}