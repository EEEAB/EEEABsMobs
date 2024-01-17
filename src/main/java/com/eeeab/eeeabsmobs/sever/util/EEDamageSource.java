package com.eeeab.eeeabsmobs.sever.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

public class EEDamageSource extends DamageSource {
    public EEDamageSource(String pMessageId) {
        super(pMessageId);
    }

    public static DamageSource shamanBombing(Entity bomb, Entity caster) {
        return new IndirectEntityDamageSource("shaman_bomb", bomb, caster).setProjectile();
        //return new DamageSource(bomb.level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
        //        getHolderOrThrow(ModKey.SHAMAN_BOMBING), bomb, caster);
    }

    public static DamageSource guardianRobustAttack(Entity troll) {
        return new EntityDamageSource("guardian_robust_attack", troll);
        //return new DamageSource(troll.level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
        //        getHolderOrThrow(ModKey.ROBUST_ATTACK), troll);
    }

    public static DamageSource guardianLaserAttack(Entity laser, Entity caster) {
        return new IndirectEntityDamageSource("guardian_laser_attack", laser, caster).bypassArmor();
        //return new DamageSource(laser.level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).
        //        getHolderOrThrow(ModKey.GUARDIAN_LASER), laser, caster);
    }
}
