package com.eeeab.eeeabsmobs.sever.world.datagen.world;

import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.OptionalLong;

public class ModDimensionsProvider {
    public static void bootstrapType(BootstapContext<DimensionType> context) {
        //context.register(ModResourceKey.VOID_CRACK_TYPE, new DimensionType(
        //        OptionalLong.empty(), // fixedTime
        //        true, // hasSkylight
        //        false, // hasCeiling
        //        false, // ultraWarm
        //        false, // natural
        //        1.0, // coordinateScale
        //        false, // bedWorks
        //        false, // respawnAnchorWorks
        //        0, // minY
        //        256, // height
        //        256, // logicalHeight
        //        BlockTags.INFINIBURN_OVERWORLD, // infiniburn
        //        BuiltinDimensionTypes.OVERWORLD_EFFECTS,// effectsLocation
        //        0f, // ambientLight
        //        new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    public static void bootstrapFunction(BootstapContext<DensityFunction> context) {
        var holdergetter = context.lookup(Registries.DENSITY_FUNCTION);
        //context.register(ModResourceKey.BASE_3D_NOISE, BlendedNoise.createUnseeded(0.25D, 0.25D, 80.0D, 160.0D, 4.0D));
        //context.register(ModResourceKey.SLOPED_CHEESE_VOID_CRACK, DensityFunctions.add(new CrackIslandDensity(0L), new DensityFunctions.HolderHolder(holdergetter.getOrThrow(ModResourceKey.BASE_3D_NOISE))));
    }

    public static void bootstrapSetting(BootstapContext<NoiseGeneratorSettings> context) {
        //context.register(ModResourceKey.VOID_CRACK_NOISE_SETTING, ModNoiseSettings.voidCrack(context));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        var biomeRegistry = context.lookup(Registries.BIOME);
        var dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        var noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);
        //LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModResourceKey.VOID_CRACK_TYPE), new NoiseBasedChunkGenerator(CrackBiomeSource.create(biomeRegistry), noiseGenSettings.getOrThrow(ModResourceKey.VOID_CRACK_NOISE_SETTING)));
        LevelStem stem = null;
        //context.register(ModResourceKey.VOID_CRACK_STEM, stem);
    }
}