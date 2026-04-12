package com.eeeab.eeeabsmobs.sever.world.datagen.world;

import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModBiomesProvider {
    public static void boostrap(BootstapContext<Biome> context) {
        //context.register(ModResourceKey.PRINCIPAL_ISLAND_BIOME, landBiome(context));
    }

    public static Biome landBiome(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        //spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityInit.CORPSE.get(), 5, 4, 4));
        //BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        //BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE)
                , context.lookup(Registries.CONFIGURED_CARVER));
        //we need to follow the same order as vanilla biomes for the BiomeDefaultFeatures
        //globalOverworldGeneration(biomeBuilder);
        //BiomeDefaultFeatures.addMossyStoneBlock(biomeBuilder);
        //BiomeDefaultFeatures.addForestFlowers(biomeBuilder);
        //BiomeDefaultFeatures.addFerns(biomeBuilder);
        //BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        //biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModResourceKey.VOIDSHARD_CHECKED);
        //biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModResourceKey.DARKENED_COAL_ORE_CHECKED);
        //biomeBuilder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModResourceKey.DARKENED_IRON_ORE_CHECKED);

        //BiomeDefaultFeatures.addExtraGold(biomeBuilder);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModResourceKey.SPARSE_BLIGHTED_OAK_CHECKED);
        //BiomeDefaultFeatures.addDefaultMushrooms(biomeBuilder);
        //BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);
        //biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PINE_PLACED_KEY);
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .downfall(0f)
                .temperature(0.1f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x395E60)
                        .waterFogColor(0x2D4A4C)
                        .fogColor(0x467275)
                        .skyColor(0x5A8A8D)
                        .grassColorOverride(0x395E60)
                        .foliageColorOverride(0x2D4A4C)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build()
                ).build();
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }
}