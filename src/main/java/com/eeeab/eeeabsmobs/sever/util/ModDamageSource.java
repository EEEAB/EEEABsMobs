package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.registries.Registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class ModDamageSource {
    public static final ResourceKey<DamageType> SHAMAN_BOMBING = register("shaman_bomb");

    public static final ResourceKey<DamageType> ROBUST_ATTACK = register("guardian_robust_attack");

    public static final ResourceKey<DamageType> GUARDIAN_LASER = register("guardian_laser_attack");

    public static DamageSource shamanBombing(Entity bomb, Entity caster) {
        return new DamageSource(bomb.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModDamageSource.SHAMAN_BOMBING), bomb, caster);
    }


    public static DamageSource guardianRobustAttack(Entity troll) {
        return new DamageSource(troll.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModDamageSource.ROBUST_ATTACK), troll);
    }


    public static DamageSource guardianLaserAttack(Entity laser, Entity caster) {
        return new DamageSource(laser.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModDamageSource.GUARDIAN_LASER), laser, caster);
    }

    private static ResourceKey<DamageType> register(String key) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
