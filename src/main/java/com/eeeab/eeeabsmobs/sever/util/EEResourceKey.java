package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EEResourceKey {
    //结构资源键
    public static ResourceKey<Structure> ANCIENT_TOMB = registerStructureKey("ancient_tomb");

    //伤害源资源键
    public static final ResourceKey<DamageType> SHAMAN_BOMBING = registerDamageSourceKey("shaman_bomb");
    public static final ResourceKey<DamageType> ROBUST_ATTACK = registerDamageSourceKey("guardian_robust_attack");
    public static final ResourceKey<DamageType> GUARDIAN_LASER = registerDamageSourceKey("guardian_laser_attack");


    public static ResourceKey<Structure> registerStructureKey(String key) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    public static ResourceKey<DamageType> registerDamageSourceKey(String key) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
