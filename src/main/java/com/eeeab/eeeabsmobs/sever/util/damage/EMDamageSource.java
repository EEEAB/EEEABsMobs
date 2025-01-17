package com.eeeab.eeeabsmobs.sever.util.damage;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EMDamageSource extends DamageSource {
    public EMDamageSource(String pMessageId) {
        super(pMessageId);
    }

    public static DamageSource shamanBombing(Entity bomb, Entity caster) {
        return new IndirectEntityDamageSource("shaman_bomb", bomb, caster).setProjectile();
    }

    public static DamageSource guardianRobustAttack(Entity troll) {
        return new EntityDamageSource("guardian_robust_attack", troll);
    }

    public static DamageSource guardianLaserAttack(Entity laser, Entity caster) {
        return new IndirectEntityDamageSource("guardian_laser_attack", laser, caster).bypassArmor();
    }

    public static DamageSource immortalMagicAttack(Entity magic, Entity caster) {
        return new IndirectEntityDamageSource("immortal_magic", magic, caster).bypassMagic();
    }

    public static DamageSource immortalAttack(Entity immortal, boolean crit, boolean ignoreArmor) {
        if (ignoreArmor) {
            return new EntityDamageSource("ignore_armor_attack", immortal).bypassArmor();
        } else if (crit) {
            return new EntityDamageSource("crit_heal", immortal);
        }
        return DamageSource.mobAttack((LivingEntity) immortal);
    }
}
