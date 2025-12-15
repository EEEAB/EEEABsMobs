package com.eeeab.eeeabsmobs.sever.world.datagen.place;

import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        /* Tree Placed Features */
        register(context, ModResourceKey.EROSION_OAK_CHECKED, configuredFeatures.getOrThrow(ModResourceKey.EROSION_OAK),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.05f, 1),
                        BlockInit.EROSION_OAK_SAPLING.get()));
        register(context, ModResourceKey.MEGA_EROSION_OAK_CHECKED, configuredFeatures.getOrThrow(ModResourceKey.MEGA_EROSION_OAK),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.025f, 1),
                        BlockInit.EROSION_OAK_SAPLING.get()));
        register(context, ModResourceKey.SPARSE_BLIGHTED_OAK_CHECKED,
                configuredFeatures.getOrThrow(ModResourceKey.BLIGHTED_OAK),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.125f, 1), BlockInit.BLIGHTED_OAK_SAPLING.get()));
        /* Ores Placed Features */
        HeightRangePlacement crackCommonPlacement = HeightRangePlacement.triangle(VerticalAnchor.absolute(32), VerticalAnchor.absolute(96));
        register(context, ModResourceKey.VOIDSHARD_CHECKED, configuredFeatures.getOrThrow(ModResourceKey.VOIDSHARD), commonOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top())));
        register(context, ModResourceKey.DARKENED_COAL_ORE_CHECKED, configuredFeatures.getOrThrow(ModResourceKey.DARKENED_COAL_ORE), commonOrePlacement(18, crackCommonPlacement));
        register(context, ModResourceKey.DARKENED_IRON_ORE_CHECKED, configuredFeatures.getOrThrow(ModResourceKey.DARKENED_IRON_ORE), commonOrePlacement(8, crackCommonPlacement));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier modifier, PlacementModifier modifier1) {
        return List.of(modifier, InSquarePlacement.spread(), modifier1, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}