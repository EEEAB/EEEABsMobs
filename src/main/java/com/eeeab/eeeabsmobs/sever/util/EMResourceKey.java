package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMResourceKey {
    public static ResourceKey<Structure> GULING = registerResourceKey(Registry.STRUCTURE_REGISTRY, "guling");
    public static ResourceKey<Structure> BLOODY_ALTAR = registerResourceKey(Registry.STRUCTURE_REGISTRY, "bloody_altar");
    public static final ResourceKey<PoiType> EROSION_PORTAL = registerResourceKey(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, "erosion_portal");
    public static final ResourceKey<Level> VOID_CRACK_LEVEL = registerResourceKey(Registry.DIMENSION_REGISTRY, "void_crack");
    public static final ResourceKey<Biome> VOID_CRACK_BIOME = registerResourceKey(Registry.BIOME_REGISTRY, "void_crack");
    public static final ResourceKey<LevelStem> VOID_CRACK_STEM = registerResourceKey(Registry.LEVEL_STEM_REGISTRY, "void_crack");
    public static final ResourceKey<DimensionType> VOID_CRACK_TYPE = registerResourceKey(Registry.DIMENSION_TYPE_REGISTRY, "void_crack");

    private static <T> ResourceKey<T> registerResourceKey(ResourceKey<Registry<T>> registry, String key) {
        return ResourceKey.create(registry, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
