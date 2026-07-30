package com.eeeab.eeeabsmobs.server.entity.util.damage;

import com.eeeab.eeeabsmobs.server.util.ModResourceKey;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class ModDamageSource {
    public static DamageSource bypassArmor(Entity entity) {
        return source(entity.level().registryAccess(), ModResourceKey.BYPASS_ARMOR, entity, entity);
    }

    public static DamageSource bypassShield(Entity directEntity, Entity causingEntity) {
        return source(directEntity.level().registryAccess(), ModResourceKey.BYPASS_SHIELD, directEntity, causingEntity);
    }

    public static DamageSource bypassCoolDown(Entity entity) {
        return source(entity.level().registryAccess(), ModResourceKey.BYPASS_IFRAME, entity, entity);
    }

    public static DamageSource guardianRobustAttack(Entity entity) {
        return source(entity.level().registryAccess(), ModResourceKey.ROBUST_ATTACK, entity, entity);
    }

    public static DamageSource laser(Entity directEntity, Entity causingEntity, boolean ignoreShield, boolean ignoreArmor) {
        DamageSource source;
        if (ignoreArmor) {
            source = source(directEntity.level().registryAccess(), ModResourceKey.BYPASS_ARMOR, directEntity, causingEntity);
        } else if (ignoreShield) {
            source = bypassShield(directEntity, causingEntity);
        } else {
            source = source(directEntity.level().registryAccess(), causingEntity instanceof Player ? DamageTypes.PLAYER_ATTACK : DamageTypes.MOB_ATTACK, directEntity, causingEntity);
        }
        return source;
    }

    public static DamageSource overloadExplode(Entity directEntity, Entity causingEntity) {
        return source(directEntity.level().registryAccess(), ModResourceKey.OVERLOAD_EXPLODE, directEntity, causingEntity);
    }

    public static DamageSource surge(Entity directEntity, Entity causingEntity) {
        return source(directEntity.level().registryAccess(), ModResourceKey.SURGE, directEntity, causingEntity);
    }

    public static DamageSource immortalMagic(Entity directEntity, Entity causingEntity) {
        return source(directEntity.level().registryAccess(), ModResourceKey.IMMORTAL_MAGIC, directEntity, causingEntity);
    }

    public static DamageSource immortalAttack(Entity entity, boolean ignoreArmor) {
        if (ignoreArmor) {
            return bypassArmor(entity);
        }
        return source(entity.level().registryAccess(), DamageTypes.MOB_ATTACK, entity, entity);
    }

    public static DamageSource source(RegistryAccess registryAccess, ResourceKey<DamageType> resourceKey, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        var registry = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(registry.getHolderOrThrow(resourceKey), directEntity, causingEntity);
    }
}
