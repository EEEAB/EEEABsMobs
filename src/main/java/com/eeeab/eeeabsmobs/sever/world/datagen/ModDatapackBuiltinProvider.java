package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.world.datagen.damage.ModDamageSourceProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.place.ModConfiguredFeatures;
import com.eeeab.eeeabsmobs.sever.world.datagen.place.ModPlacedFeatures;
import com.eeeab.eeeabsmobs.sever.world.datagen.world.ModBiomesProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.world.ModDimensionsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackBuiltinProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, ModDamageSourceProvider::boostrap)
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(Registries.BIOME, ModBiomesProvider::boostrap)
            //.add(ForgeRegistries.Keys.BIOME_MODIFIERS, EMBiomeModifierProvider::bootstrap)
            .add(Registries.DIMENSION_TYPE, ModDimensionsProvider::bootstrapType)
            .add(Registries.DENSITY_FUNCTION, ModDimensionsProvider::bootstrapFunction)
            .add(Registries.NOISE_SETTINGS, ModDimensionsProvider::bootstrapSetting)
            .add(Registries.LEVEL_STEM, ModDimensionsProvider::bootstrapStem);

    public ModDatapackBuiltinProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(EEEABMobs.MOD_ID));
    }
}