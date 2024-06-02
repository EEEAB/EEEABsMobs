package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMResourceKey {
    public static ResourceKey<Structure> GULING = registerResourceKey(Registries.STRUCTURE, "guling");
    public static ResourceKey<Structure> BLOODY_ALTAR = registerResourceKey(Registries.STRUCTURE, "bloody_altar");
    public static final ResourceKey<DamageType> SHAMAN_BOMBING = registerResourceKey(Registries.DAMAGE_TYPE, "shaman_bomb");
    public static final ResourceKey<DamageType> ROBUST_ATTACK = registerResourceKey(Registries.DAMAGE_TYPE, "guardian_robust_attack");
    public static final ResourceKey<DamageType> GUARDIAN_LASER = registerResourceKey(Registries.DAMAGE_TYPE, "guardian_laser_attack");
    public static final ResourceKey<PoiType> EROSION_PORTAL = registerResourceKey(Registries.POINT_OF_INTEREST_TYPE, "erosion_portal");
    public static final ResourceKey<Level> VOID_CRACK_LEVEL = registerResourceKey(Registries.DIMENSION, "void_crack");
    public static final ResourceKey<Biome> VOID_CRACK_BIOME = registerResourceKey(Registries.BIOME, "void_crack");
    public static final ResourceKey<LevelStem> VOID_CRACK_STEM = registerResourceKey(Registries.LEVEL_STEM, "void_crack");
    public static final ResourceKey<DimensionType> VOID_CRACK_TYPE = registerResourceKey(Registries.DIMENSION_TYPE, "void_crack");

    private static <T> ResourceKey<T> registerResourceKey(ResourceKey<Registry<T>> registry, String key) {
        return ResourceKey.create(registry, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
