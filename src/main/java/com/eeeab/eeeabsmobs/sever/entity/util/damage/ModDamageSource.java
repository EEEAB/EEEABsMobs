package com.eeeab.eeeabsmobs.sever.entity.util.damage;

import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;

public class ModDamageSource {
    public static DamageSource shamanBombing(Entity bomb, Entity caster) {
        return new DamageSource(bomb.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModResourceKey.SHAMAN_BOMBING), bomb, caster);
    }

    public static DamageSource guardianRobustAttack(Entity troll) {
        return new DamageSource(troll.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModResourceKey.ROBUST_ATTACK), troll);
    }

    public static DamageSource guardianLaserAttack(Entity laser, Entity caster) {
        return new DamageSource(laser.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModResourceKey.GUARDIAN_LASER), laser, caster);
    }

    public static DamageSource overloadExplode(Entity causingEntity, Entity directEntity) {
        return new DamageSource(causingEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModResourceKey.OVERLOAD_EXPLODE), causingEntity, directEntity);
    }

    public static DamageSource immortalMagicAttack(Entity magic, Entity caster) {
        return new DamageSource(magic.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModResourceKey.IMMORTAL_MAGIC), magic, caster);
    }

    public static DamageSource immortalAttack(Entity immortal, boolean crit, boolean ignoreArmor) {
        return new DamageSource(immortal.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(
                ignoreArmor ? ModResourceKey.IGNORE_ARMOR_ATTACK : crit ? ModResourceKey.CRIT_HEAL : DamageTypes.MOB_ATTACK), immortal);
    }
}
