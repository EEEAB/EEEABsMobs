package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.*;

public class EffectArmorLower extends EffectBase {
    private final double multiplier;

    public EffectArmorLower(MobEffectCategory type, int color, boolean isInstant, double multiplier) {
        super(type, color, isInstant);
        this.multiplier = multiplier;
    }

    @Override
    protected boolean canApplyEffect(int remainingTicks, int level) {
        return remainingTicks % 10 == 0;
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        if (amplifier < 4) {
            return this.multiplier * (double) (amplifier + 1);
        }
        return -(double) Integer.MAX_VALUE;
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }
}
