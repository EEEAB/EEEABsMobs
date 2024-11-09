package com.eeeab.eeeabsmobs.sever.util.damage;

import com.eeeab.eeeabsmobs.sever.util.EMResourceKey;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class DamageSourceProvider {

    public static void boostrap(BootstapContext<DamageType> context) {
        ResourceKey<DamageType> shamanBombing = EMResourceKey.SHAMAN_BOMBING;
        ResourceKey<DamageType> guardianLaser = EMResourceKey.GUARDIAN_LASER;
        ResourceKey<DamageType> robustAttack = EMResourceKey.ROBUST_ATTACK;
        ResourceKey<DamageType> immortalMagic = EMResourceKey.IMMORTAL_MAGIC;
        ResourceKey<DamageType> ignoreArmorAttack = EMResourceKey.IGNORE_ARMOR_ATTACK;
        ResourceKey<DamageType> critHeal = EMResourceKey.CRIT_HEAL;

        context.register(shamanBombing, new DamageType(getMsgId(shamanBombing), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(guardianLaser, new DamageType(getMsgId(guardianLaser), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
        context.register(robustAttack, new DamageType(getMsgId(robustAttack), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(immortalMagic, new DamageType(getMsgId(immortalMagic), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
        context.register(ignoreArmorAttack, new DamageType(getMsgId(ignoreArmorAttack), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(critHeal, new DamageType(getMsgId(critHeal), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
    }

    private static String getMsgId(ResourceKey<DamageType> key) {
        return key.location().toLanguageKey();
    }
}
