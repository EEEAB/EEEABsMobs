package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner;

import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class CopyOwnerTargetGoal extends TargetGoal {
    private final EntityImmortal immortal;
    private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    public CopyOwnerTargetGoal(EntityImmortal mob) {
        super(mob, false);
        this.immortal = mob;
    }

    @Override
    public boolean canUse() {
        return immortal.getOwner() != null && immortal.getOwner().getTarget() != null && this.canAttack(immortal.getOwner().getTarget(), this.copyOwnerTargeting);
    }

    @Override
    public void start() {
        if (immortal.getOwner() != null && immortal.getOwner().getTarget() != null)
            immortal.setTarget(immortal.getOwner().getTarget());
        super.start();
    }
}
