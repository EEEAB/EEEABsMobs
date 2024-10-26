package com.eeeab.eeeabsmobs.sever.potion;

import com.eeeab.eeeabsmobs.sever.init.AttributeInit;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

//侵蚀
public class EffectErode extends EMEffect {
    private final Random random = new Random();

    public EffectErode() {
        super(MobEffectCategory.HARMFUL, 1648179, false);
        this.addAttributeModifier(AttributeInit.CRIT_CHANCE.get(), "023793E5-DB53-4724-AC5F-3B15059C5079", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    protected boolean canApplyEffect(int remainingTicks, int level) {
        return remainingTicks % 10 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplified) {
        amplified = amplified + 1;
        int i = random.nextInt(3);
        if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) return;
        if (i == 0) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    DamageArmor(slot, entity, amplified);
                }
            }
        }
    }

    private void DamageArmor(EquipmentSlot slot, LivingEntity entity, int amplified) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (!stack.isDamaged()) return;
        stack.hurtAndBreak(amplified, entity, e -> e.broadcastBreakEvent(slot));
    }


    @Override
    public boolean isBeneficial() {
        return false;
    }
}

