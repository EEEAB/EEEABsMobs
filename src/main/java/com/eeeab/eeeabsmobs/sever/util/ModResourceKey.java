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
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModResourceKey {
    public static ResourceKey<Structure> GULING = registerResourceKey(Registries.STRUCTURE, "guling");
    public static ResourceKey<Structure> BLOODY_ALTAR = registerResourceKey(Registries.STRUCTURE, "bloody_altar");

    public static final ResourceKey<DamageType> SHAMAN_BOMBING = registerResourceKey(Registries.DAMAGE_TYPE, "shaman_bomb");
    public static final ResourceKey<DamageType> ROBUST_ATTACK = registerResourceKey(Registries.DAMAGE_TYPE, "guardian_robust_attack");
    public static final ResourceKey<DamageType> OVERLOAD_EXPLODE = registerResourceKey(Registries.DAMAGE_TYPE, "overload_explode");
    public static final ResourceKey<DamageType> IMMORTAL_MAGIC = registerResourceKey(Registries.DAMAGE_TYPE, "immortal_magic");
    public static final ResourceKey<DamageType> IGNORE_ARMOR_ATTACK = registerResourceKey(Registries.DAMAGE_TYPE, "ignore_armor_attack");
    public static final ResourceKey<DamageType> IGNORE_SHIELD_ATTACK = registerResourceKey(Registries.DAMAGE_TYPE, "ignore_shield_attack");
    public static final ResourceKey<DamageType> CRIT_HEAL = registerResourceKey(Registries.DAMAGE_TYPE, "crit_heal");

    public static final ResourceKey<ConfiguredFeature<?, ?>> BLIGHTED_OAK = registerResourceKey(Registries.CONFIGURED_FEATURE, "blighted_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DARKENED_COAL_ORE = registerResourceKey(Registries.CONFIGURED_FEATURE, "darkened_coal_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DARKENED_IRON_ORE = registerResourceKey(Registries.CONFIGURED_FEATURE, "darkened_iron_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VOIDSHARD = registerResourceKey(Registries.CONFIGURED_FEATURE, "voidshard");

    public static final ResourceKey<PlacedFeature> SPARSE_BLIGHTED_OAK_CHECKED = registerResourceKey(Registries.PLACED_FEATURE, "blighted_oak_checked");
    public static final ResourceKey<PlacedFeature> DARKENED_COAL_ORE_CHECKED = registerResourceKey(Registries.PLACED_FEATURE, "darkened_coal_ore_checked");
    public static final ResourceKey<PlacedFeature> DARKENED_IRON_ORE_CHECKED = registerResourceKey(Registries.PLACED_FEATURE, "darkened_iron_ore_checked");
    public static final ResourceKey<PlacedFeature> VOIDSHARD_CHECKED = registerResourceKey(Registries.PLACED_FEATURE, "voidshard_checked");

    public static final ResourceKey<PoiType> EROSION_PORTAL = registerResourceKey(Registries.POINT_OF_INTEREST_TYPE, "erosion_portal");
    public static final ResourceKey<Biome> PRINCIPAL_ISLAND_BIOME = registerResourceKey(Registries.BIOME, "principal_island_biome");
    public static final ResourceKey<Level> VOID_CRACK_LEVEL = registerResourceKey(Registries.DIMENSION, "void_crack");
    public static final ResourceKey<LevelStem> VOID_CRACK_STEM = registerResourceKey(Registries.LEVEL_STEM, "void_crack");
    public static final ResourceKey<DimensionType> VOID_CRACK_TYPE = registerResourceKey(Registries.DIMENSION_TYPE, "void_crack");

    public static final ResourceKey<NoiseGeneratorSettings> VOID_CRACK_NOISE_SETTING = registerResourceKey(Registries.NOISE_SETTINGS, "void_crack_noise_setting");
    public static final ResourceKey<DensityFunction> BASE_3D_NOISE = registerResourceKey(Registries.DENSITY_FUNCTION, "void_crack/base_3d_noise");
    public static final ResourceKey<DensityFunction> SLOPED_CHEESE_VOID_CRACK = registerResourceKey(Registries.DENSITY_FUNCTION, "void_crack/sloped_cheese");

    private static <T> ResourceKey<T> registerResourceKey(ResourceKey<Registry<T>> registry, String key) {
        return ResourceKey.create(registry, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
