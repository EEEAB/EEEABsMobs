package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.util.MTUtils;
import net.minecraft.network.chat.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ImmortalSword extends SwordItem {
    public ImmortalSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(MTUtils.UNABLE_BREAKS);
        tooltip.add(MTUtils.simpleWeaponText("immortal_sword", MTUtils.STYLE_GRAY));
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity entity) {
        target.addEffect(new MobEffectInstance(EffectInit.ERODE_EFFECT.get(), 100, 0));
        return super.hurtEnemy(itemStack, target, entity);
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }
}
