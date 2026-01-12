package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobEntity;
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
        return super.canUse() && this.mobEntity.isAlive() && this.mobEntity.active;
    }

    @Override
    public boolean canContinueToUse() {return super.canContinueToUse() && this.mobEntity.isAlive() && this.mobEntity.active;}
}
