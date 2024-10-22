package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

//狂乱
public class EffectFrenzy extends EMEffect {
    public EffectFrenzy() {
        super(MobEffectCategory.NEUTRAL, 16711680, false);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "3039A932-0AF9-E41C-2DD5-996DCCF1E8A0", 1, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "CB12110E-C2D2-2E2B-3F2E-97956CB5A564", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "2CC33735-EBB1-497C-B893-8A6956FE5257", 0.2, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), "1B5B2365-FB46-FA20-E5A6-AC10EFE2E387", 1, AttributeModifier.Operation.ADDITION);
    }
}
