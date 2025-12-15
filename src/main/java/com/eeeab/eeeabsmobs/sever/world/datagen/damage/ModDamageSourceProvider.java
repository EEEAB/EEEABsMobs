package com.eeeab.eeeabsmobs.sever.world.datagen.damage;

import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageSourceProvider {
    public static void boostrap(BootstapContext<DamageType> context) {
        ResourceKey<DamageType> shamanBombing = ModResourceKey.SHAMAN_BOMBING;
        ResourceKey<DamageType> guardianLaser = ModResourceKey.GUARDIAN_LASER;
        ResourceKey<DamageType> robustAttack = ModResourceKey.ROBUST_ATTACK;
        ResourceKey<DamageType> overloadExplode = ModResourceKey.OVERLOAD_EXPLODE;
        ResourceKey<DamageType> immortalMagic = ModResourceKey.IMMORTAL_MAGIC;
        ResourceKey<DamageType> ignoreArmorAttack = ModResourceKey.IGNORE_ARMOR_ATTACK;
        ResourceKey<DamageType> critHeal = ModResourceKey.CRIT_HEAL;

        context.register(shamanBombing, new DamageType(getMsgId(shamanBombing), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(guardianLaser, new DamageType(getMsgId(guardianLaser), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
        context.register(robustAttack, new DamageType(getMsgId(robustAttack), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(overloadExplode, new DamageType(getMsgId(overloadExplode), DamageScaling.ALWAYS, 0.1F));
        context.register(immortalMagic, new DamageType(getMsgId(immortalMagic), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
        context.register(ignoreArmorAttack, new DamageType(getMsgId(ignoreArmorAttack), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(critHeal, new DamageType(getMsgId(critHeal), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
    }

    private static String getMsgId(ResourceKey<DamageType> key) {
        return key.location().toLanguageKey();
    }
}
