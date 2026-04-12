package com.eeeab.eeeabsmobs.sever.world.datagen.place;

import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;

public class ModBiomeModifierProvider {
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        context.register(ModResourceKey.ADD_ANCIENT_BOUNDARY_STONE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModResourceKey.ANCIENT_BOUNDARY_STONE_CHECKED)),
                GenerationStep.Decoration.UNDERGROUND_ORES));
    }
}
