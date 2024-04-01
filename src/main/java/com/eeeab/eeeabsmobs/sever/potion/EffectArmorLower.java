package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.*;

//护甲无效化
public class EffectArmorLower extends EMEffect {
    private final double multiplier;

    public EffectArmorLower() {
        super(MobEffectCategory.HARMFUL, 4879982, false);
        this.addAttributeModifier(Attributes.ARMOR, "0eacf76e-1e90-42e6-9479-2a5af12dd6cf", 0.0D, AttributeModifier.Operation.ADDITION);
        this.multiplier = -6.0D;
    }

    @Override
    protected boolean canApplyEffect(int remainingTicks, int level) {
        return remainingTicks % 10 == 0;
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        double level = Math.min(amplifier + 1D, 5D);
        if (Attributes.ARMOR instanceof RangedAttribute attribute) {
            return attribute.getMaxValue() * -(level * 0.2D);
        }
        return this.multiplier * level;//理论无法到达这里
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }
}
