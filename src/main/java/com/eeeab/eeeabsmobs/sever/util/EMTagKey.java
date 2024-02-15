package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMTagKey {
    public static final TagKey<Structure> EYE_OF_ANCIENT_TOMB = registerStructureTag("eye_of_ancient_tomb");
    public static final TagKey<Biome> ANCIENT_TOMB_BIOMES = registerBiomeTag("ancient_tomb_biomes");
    public static final TagKey<DamageType> GENERAL_UNRESISTANT_TO = registerDamageTypeTag("general_unresistant_to");
    public static final TagKey<DamageType> MAGIC_UNRESISTANT_TO = registerDamageTypeTag("magic_unresistant_to");
    public static final TagKey<EntityType<?>> TRAP_WHITELIST = registerEntityTypeTag("trap_whitelist");

    private static TagKey<DamageType> registerDamageTypeTag(String key) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    private static TagKey<Structure> registerStructureTag(String key) {
        return TagKey.create(Registries.STRUCTURE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    private static TagKey<Biome> registerBiomeTag(String key) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    private static TagKey<EntityType<?>> registerEntityTypeTag(String key) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
