package com.eeeab.eeeabsmobs.sever.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class ModDamageSource {
    public static DamageSource shamanBombing(Entity bomb, Entity caster) {
        return new DamageSource(bomb.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModKey.SHAMAN_BOMBING), bomb, caster);
    }

    public static DamageSource guardianRobustAttack(Entity troll) {
        return new DamageSource(troll.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModKey.ROBUST_ATTACK), troll);
    }

    public static DamageSource guardianLaserAttack(Entity laser, Entity caster) {
        return new DamageSource(laser.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModKey.GUARDIAN_LASER), laser, caster);
    }
}
