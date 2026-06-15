package com.eeeab.eeeabsmobs.server.world.datagen.damage;

import com.eeeab.eeeabsmobs.server.util.ModResourceKey;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageSourceProvider {
    public static void boostrap(BootstapContext<DamageType> context) {
        ResourceKey<DamageType> bypassArmor = ModResourceKey.BYPASS_ARMOR;
        ResourceKey<DamageType> bypassShield = ModResourceKey.BYPASS_SHIELD;
        ResourceKey<DamageType> bypassIframe = ModResourceKey.BYPASS_IFRAME;
        ResourceKey<DamageType> robustAttack = ModResourceKey.ROBUST_ATTACK;
        ResourceKey<DamageType> overloadExplode = ModResourceKey.OVERLOAD_EXPLODE;
        ResourceKey<DamageType> surge = ModResourceKey.SURGE;
        ResourceKey<DamageType> immortalMagic = ModResourceKey.IMMORTAL_MAGIC;

        context.register(bypassArmor, new DamageType(getMsgId(bypassArmor), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(bypassShield, new DamageType(getMsgId(bypassShield), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(bypassIframe, new DamageType(getMsgId(bypassIframe), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(robustAttack, new DamageType(getMsgId(robustAttack), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(overloadExplode, new DamageType(getMsgId(overloadExplode), DamageScaling.ALWAYS, 0.1F));
        context.register(surge, new DamageType(getMsgId(surge), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(immortalMagic, new DamageType(getMsgId(immortalMagic), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
    }

    private static String getMsgId(ResourceKey<DamageType> key) {
        return key.location().toLanguageKey();
    }
}
