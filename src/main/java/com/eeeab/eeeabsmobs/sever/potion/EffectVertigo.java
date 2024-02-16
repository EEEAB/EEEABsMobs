package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffectCategory;

public class EffectVertigo extends EMEffect {
    public EffectVertigo(MobEffectCategory type, int color, boolean isInstant) {
        super(type, color, isInstant);
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }
}
