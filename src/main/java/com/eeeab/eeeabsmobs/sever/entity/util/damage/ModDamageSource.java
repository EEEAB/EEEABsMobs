package com.eeeab.eeeabsmobs.sever.entity.util.damage;

import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ModDamageSource {
    public static DamageSource bypassArmor(Entity entity) {
        return new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(
                ModResourceKey.BYPASS_ARMOR), entity);
    }

    public static DamageSource bypassShield(Entity directEntity, Entity causingEntity) {
        return new DamageSource(directEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(
                ModResourceKey.BYPASS_SHIELD), directEntity, causingEntity);
    }

    public static DamageSource bypassCoolDown(Entity entity) {
        return new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(
                ModResourceKey.BYPASS_IFRAME), entity);
    }

    public static DamageSource guardianRobustAttack(Entity troll) {
        return new DamageSource(troll.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModResourceKey.ROBUST_ATTACK), troll);
    }

    public static DamageSource laser(Entity laser, Entity caster, boolean ignoreShield, boolean ignoreArmor) {
        return new DamageSource(laser.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(
                ignoreArmor ? ModResourceKey.BYPASS_ARMOR : ignoreShield ? ModResourceKey.BYPASS_SHIELD :
                        caster instanceof Player ? DamageTypes.PLAYER_ATTACK : DamageTypes.MOB_ATTACK), laser, caster);
    }

    public static DamageSource overloadExplode(Entity directEntity, Entity causingEntity) {
        return new DamageSource(causingEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModResourceKey.OVERLOAD_EXPLODE), directEntity, causingEntity);
    }

    public static DamageSource immortalMagic(Entity magic, Entity caster) {
        return new DamageSource(magic.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
                getHolderOrThrow(ModResourceKey.IMMORTAL_MAGIC), magic, caster);
    }

    public static DamageSource immortalAttack(Entity immortal, boolean ignoreArmor) {
        return new DamageSource(immortal.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(
                ignoreArmor ? ModResourceKey.BYPASS_ARMOR : DamageTypes.MOB_ATTACK), immortal);
    }
}
