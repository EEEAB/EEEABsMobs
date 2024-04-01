package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMTagKey {
    public static final TagKey<Structure> EYE_OF_ANCIENT_TOMB = registerTagKey(Registries.STRUCTURE, "eye_of_ancient_tomb");
    public static final TagKey<Structure> EYE_OF_BLOODY_ALTAR = registerTagKey(Registries.STRUCTURE, "eye_of_bloody_altar");
    public static final TagKey<Biome> ANCIENT_TOMB_BIOMES = registerTagKey(Registries.BIOME, "ancient_tomb_biomes");
    public static final TagKey<Biome> BLOODY_ALTAR_BIOMES = registerTagKey(Registries.BIOME, "bloody_altar_biomes");
    public static final TagKey<DamageType> GENERAL_UNRESISTANT_TO = registerTagKey(Registries.DAMAGE_TYPE, "general_unresistant_to");
    public static final TagKey<DamageType> MAGIC_UNRESISTANT_TO = registerTagKey(Registries.DAMAGE_TYPE, "magic_unresistant_to");
    public static final TagKey<EntityType<?>> TRAP_WHITELIST = registerTagKey(Registries.ENTITY_TYPE, "trap_whitelist");

    private static <T> TagKey<T> registerTagKey(ResourceKey<Registry<T>> registry, String key) {
        return TagKey.create(registry, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
