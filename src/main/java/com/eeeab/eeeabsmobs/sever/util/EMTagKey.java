package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMTagKey {
    public static final TagKey<Structure> EYE_OF_GULING = registerTagKey(Registry.STRUCTURE_REGISTRY, "eye_of_guling");
    public static final TagKey<Structure> EYE_OF_BLOODY_ALTAR = registerTagKey(Registry.STRUCTURE_REGISTRY, "eye_of_bloody_altar");
    public static final TagKey<Biome> GULING_BIOMES = registerTagKey(Registry.BIOME_REGISTRY, "guling_biomes");
    public static final TagKey<Biome> BLOODY_ALTAR_BIOMES = registerTagKey(Registry.BIOME_REGISTRY, "bloody_altar_biomes");
    public static final TagKey<EntityType<?>> TRAP_WHITELIST = registerTagKey(Registry.ENTITY_TYPE_REGISTRY, "trap_whitelist");
    public static final TagKey<EntityType<?>> RESISTS_FORCED_CHANGE_TARGET = registerTagKey(Registry.ENTITY_TYPE_REGISTRY, "resists_forced_change_target");
    public static final TagKey<EntityType<?>> IMMORTAL_ARMY = registerTagKey(Registry.ENTITY_TYPE_REGISTRY, "immortal_army");
    public static final TagKey<Item> DEMOLISHER_SUPPORTED_PROJECTILES = registerTagKey(Registry.ITEM_REGISTRY, "demolisher_supported_projectiles");

    private static <T> TagKey<T> registerTagKey(ResourceKey<Registry<T>> registry, String key) {
        return TagKey.create(registry, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    //Other Mod
    private static final String ICE_AND_FIRE = "iceandfire";
    private static final String ALEX_S_CAVES = "alexscaves";
    private static final String CATACLYSM = "cataclysm";
    public static final TagKey<EntityType<?>> BLINDED = registerOtherModTagKey(Registry.ENTITY_TYPE_REGISTRY, ICE_AND_FIRE, "blinded");
    public static final TagKey<EntityType<?>> IMMUNE_TO_GORGON_STONE = registerOtherModTagKey(Registry.ENTITY_TYPE_REGISTRY, ICE_AND_FIRE, "immune_to_gorgon_stone");
    public static final TagKey<EntityType<?>> RESISTS_RADIATION = registerOtherModTagKey(Registry.ENTITY_TYPE_REGISTRY, ALEX_S_CAVES, "resists_radiation");
    public static final TagKey<EntityType<?>> RESISTS_TOTEM_OF_POSSESSION = registerOtherModTagKey(Registry.ENTITY_TYPE_REGISTRY, ALEX_S_CAVES, "resists_totem_of_possession");
    public static final TagKey<EntityType<?>> RESISTS_TREMORSAURUS_ROAR = registerOtherModTagKey(Registry.ENTITY_TYPE_REGISTRY, ALEX_S_CAVES, "resists_tremorsaurus_roar");
    public static final TagKey<MobEffect> EFFECTIVE_FOR_BOSSES = registerOtherModTagKey(Registry.MOB_EFFECT_REGISTRY, CATACLYSM, "effective_for_bosses");

    private static <T> TagKey<T> registerOtherModTagKey(ResourceKey<Registry<T>> registry, String modId, String key) {
        return TagKey.create(registry, new ResourceLocation(modId, key));
    }
}
