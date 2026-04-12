package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

//狂乱
public class FrenzyEffect extends ModEffect {
    public FrenzyEffect() {
        super(MobEffectCategory.NEUTRAL, 16711680, false);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "3039A932-0AF9-E41C-2DD5-996DCCF1E8A0", 1, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ARMOR, "7A925881-9C09-43E2-AA53-B032308413F6", 1, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "CB12110E-C2D2-2E2B-3F2E-97956CB5A564", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "2CC33735-EBB1-497C-B893-8A6956FE5257", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
