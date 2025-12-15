package com.eeeab.eeeabsmobs.sever.world.datagen.place;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraftforge.common.world.BiomeModifier;

public class ModBiomeModifierProvider {
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeature = context.lookup(Registries.PLACED_FEATURE);
        var biome = context.lookup(Registries.BIOME);
    }
}
