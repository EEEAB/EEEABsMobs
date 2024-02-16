package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMTagKey {
    public static final TagKey<Structure> EYE_OF_ANCIENT_TOMB = registerStructureTag("eye_of_ancient_tomb");
    public static final TagKey<Biome> ANCIENT_TOMB_BIOMES = registerBiomeTag("ancient_tomb_biomes");
    public static final TagKey<EntityType<?>> TRAP_WHITELIST = registerEntityTypeTag("trap_whitelist");

    private static TagKey<Structure> registerStructureTag(String key) {
        return TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    private static TagKey<Biome> registerBiomeTag(String key) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    private static TagKey<EntityType<?>> registerEntityTypeTag(String key) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
