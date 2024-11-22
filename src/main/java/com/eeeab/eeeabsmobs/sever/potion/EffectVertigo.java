package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

//眩晕
public class EffectVertigo extends EMEffect {
    public EffectVertigo() {
        super(MobEffectCategory.HARMFUL, 13434675, false);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "F2410272-55FA-4A61-B619-3C4359D3CD23", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }
}
