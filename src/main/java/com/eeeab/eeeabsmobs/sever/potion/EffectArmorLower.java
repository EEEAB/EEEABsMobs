package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.*;

//护甲无效化
public class EffectArmorLower extends EMEffect {

    public EffectArmorLower() {
        super(MobEffectCategory.HARMFUL, 7004023, false);
        this.addAttributeModifier(Attributes.ARMOR, "0eacf76e-1e90-42e6-9479-2a5af12dd6cf", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "fb595864-b59d-4c57-aa85-7879268e3a4c", -0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    protected boolean canApplyEffect(int remainingTicks, int level) {
        return remainingTicks > 0;
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }
}
