package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMResourceKey {
    public static ResourceKey<Structure> ANCIENT_TOMB = registerStructureKey("ancient_tomb");
    public static final ResourceKey<DamageType> SHAMAN_BOMBING = registerDamageSourceKey("shaman_bomb");
    public static final ResourceKey<DamageType> ROBUST_ATTACK = registerDamageSourceKey("guardian_robust_attack");
    public static final ResourceKey<DamageType> GUARDIAN_LASER = registerDamageSourceKey("guardian_laser_attack");
    public static final ResourceKey<PoiType> EROSION_PORTAL = registerPoiTypeKey("erosion_portal");
    public static final ResourceKey<Level> EROSION = registerWorldKey("erosion");


    public static ResourceKey<Structure> registerStructureKey(String key) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    public static ResourceKey<DamageType> registerDamageSourceKey(String key) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    public static ResourceKey<PoiType> registerPoiTypeKey(String key) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    public static ResourceKey<Level> registerWorldKey(String key) {
        return ResourceKey.create(Registries.DIMENSION, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
