package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMTagKey {
    public static final TagKey<Structure> EYE_OF_GULING = registerTagKey(Registry.STRUCTURE_REGISTRY, "eye_of_guling");
    public static final TagKey<Structure> EYE_OF_BLOODY_ALTAR = registerTagKey(Registry.STRUCTURE_REGISTRY, "eye_of_bloody_altar");
    public static final TagKey<Biome> GULING_BIOMES = registerTagKey(Registry.BIOME_REGISTRY, "guling_biomes");
    public static final TagKey<Biome> BLOODY_ALTAR_BIOMES = registerTagKey(Registry.BIOME_REGISTRY, "bloody_altar_biomes");
    public static final TagKey<EntityType<?>> TRAP_WHITELIST = registerTagKey(Registry.ENTITY_TYPE_REGISTRY, "trap_whitelist");

    private static <T> TagKey<T> registerTagKey(ResourceKey<Registry<T>> registry, String key) {
        return TagKey.create(registry, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
