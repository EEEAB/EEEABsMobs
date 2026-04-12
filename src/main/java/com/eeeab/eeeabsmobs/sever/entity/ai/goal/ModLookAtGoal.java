package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobEntity;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class ModLookAtGoal extends LookAtPlayerGoal {
    private final EEEABMobEntity mobEntity;

    public ModLookAtGoal(EEEABMobEntity mob, Class<? extends LivingEntity> aClass, float distance) {
        this(mob, aClass, distance, false);
    }

    public ModLookAtGoal(EEEABMobEntity mob, Class<? extends LivingEntity> aClass, float distance, boolean stare) {
        super(mob, aClass, distance, stare ? 1F : 0.02F);
        this.mobEntity = mob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.test();
    }

    @Override
    public boolean canContinueToUse() {
        return this.test() && super.canContinueToUse();
    }

    private boolean test() {
        return this.mobEntity.isAlive() && this.mobEntity.active && !this.mobEntity.hasEffect(EffectInit.STUN_EFFECT.get());
    }
}
