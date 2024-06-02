package com.eeeab.eeeabsmobs.sever.world.dimension;

import com.eeeab.eeeabsmobs.sever.util.EMResourceKey;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class DimensionsCrackProvider {

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(EMResourceKey.VOID_CRACK_TYPE, new DimensionType(
                OptionalLong.empty()/*OptionalLong.of(12000)*/, // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                1.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);
        HolderGetter<PlacedFeature> placedFeatureRegistry = context.lookup(Registries.PLACED_FEATURE);

        //NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
        //        new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.TEST_BIOME)),
        //        noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(/*Pair.of(
                                        Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.TEST_BIOME)),*/
                                Pair.of(
                                        Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.BIRCH_FOREST)),
                                Pair.of(
                                        Climate.parameters(0.3F, 0.6F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.OCEAN)),
                                Pair.of(
                                        Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.DARK_FOREST))

                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(Optional.empty(), biomeRegistry.getOrThrow(EMResourceKey.VOID_CRACK_BIOME), List.of
                (
                        placedFeatureRegistry.getOrThrow(
                                MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND
                        )
                        , placedFeatureRegistry.getOrThrow(
                                MiscOverworldPlacements.LAKE_LAVA_SURFACE
                        )
                ));
        FlatLevelSource flatLevelSource = new FlatLevelSource(settings);
        settings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
        settings.getLayersInfo().add(new FlatLayerInfo(2, Blocks.DIRT));
        settings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
        settings.updateLayers();

        //LevelStem stem = new LevelStem(dimTypes.getOrThrow(DimensionsProvider.KAUPEN_DIM_TYPE), noiseBasedChunkGenerator);
        LevelStem stem = new LevelStem(dimTypes.getOrThrow(EMResourceKey.VOID_CRACK_TYPE), flatLevelSource);

        context.register(EMResourceKey.VOID_CRACK_STEM, stem);
    }
}