package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ModEffect extends MobEffect {
    private final boolean instant;

    public ModEffect(MobEffectCategory type, int color, boolean isInstant) {
        super(type, color);
        this.instant = isInstant;
    }

    @Override
    public boolean isDurationEffectTick(int remainingTicks, int level) {
        if (isInstantenous()) {
            return true;
        }
        return canApplyEffect(remainingTicks, level);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (isInstantenous()) {
            applyInstantenousEffect(null, null, entity, amplifier, 1.0d);
        }
    }

    public boolean isInstantenous() {
        return instant;
    }

    protected boolean canApplyEffect(int remainingTicks, int level) {
        return false;
    }
}


