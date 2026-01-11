package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.world.level.CrackBiomeSource;
import com.eeeab.eeeabsmobs.sever.world.level.feature.EmptyFoliagePlacer;
import com.eeeab.eeeabsmobs.sever.world.level.feature.WitheredTrunkPlacer;
import com.eeeab.eeeabsmobs.sever.world.level.noise.CrackIslandDensity;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WorldGenInit {
    public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(Registries.BIOME_SOURCE, EEEABMobs.MOD_ID);
    public static final DeferredRegister<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, EEEABMobs.MOD_ID);
    public static final DeferredRegister<TrunkPlacerType<?>> TREE_TRUNK_PLACER_TYPES = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, EEEABMobs.MOD_ID);
    //public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, EEEABMobs.MOD_ID);
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_PLACER = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, EEEABMobs.MOD_ID);
    public static final RegistryObject<TrunkPlacerType<WitheredTrunkPlacer>> DEAD_TREE = TREE_TRUNK_PLACER_TYPES.register("dead_tree", () -> new TrunkPlacerType<>(WitheredTrunkPlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<EmptyFoliagePlacer>> EMPTY_LEAVE = FOLIAGE_PLACER_PLACER.register("empty_leave", () -> new FoliagePlacerType<>(EmptyFoliagePlacer.CODEC));

    static {
        BIOME_SOURCES.register("void_crack", () -> CrackBiomeSource.CODEC);
        DENSITY_FUNCTION_TYPES.register("void_crack_islands", CrackIslandDensity.CODEC::codec);
    }

    public static void register(IEventBus bus) {
        BIOME_SOURCES.register(bus);
        DENSITY_FUNCTION_TYPES.register(bus);
        TREE_TRUNK_PLACER_TYPES.register(bus);
        //TREE_DECORATOR_TYPES.register(bus);
        FOLIAGE_PLACER_PLACER.register(bus);
    }
}