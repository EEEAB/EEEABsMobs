package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class EffectErode extends EffectBase {
    private final Random random = new Random();

    public EffectErode() {
        super(MobEffectCategory.HARMFUL, -15132648, false);
    }

    @Override
    protected boolean canApplyEffect(int remainingTicks, int level) {
        return remainingTicks % 10 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplified) {
        amplified = amplified + 1;
        int i = random.nextInt(3) + 1;
        if (i == 2) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    DamageArmor(slot, entity, amplified);
                }
            }
        }
    }

    private void DamageArmor(EquipmentSlot slot, LivingEntity entity, int amplified) {
        ItemStack stack = entity.getItemBySlot(slot);
        stack.hurtAndBreak(amplified, entity, (e) -> {
            e.broadcastBreakEvent(slot);
        });
    }


    @Override
    public boolean isBeneficial() {
        return false;
    }
}

