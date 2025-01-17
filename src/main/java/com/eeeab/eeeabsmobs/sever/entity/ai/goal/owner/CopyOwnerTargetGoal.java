package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner;

import com.eeeab.eeeabsmobs.sever.entity.VenerableEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class CopyOwnerTargetGoal<T extends Mob & VenerableEntity<T>> extends TargetGoal {
    private final T venerable;
    private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    public CopyOwnerTargetGoal(T venerable) {
        super(venerable, false);
        this.venerable = venerable;
    }

    @Override
    public boolean canUse() {
        return venerable.getOwner() != null && venerable.getOwner().getTarget() != null && this.canAttack(venerable.getOwner().getTarget(), this.copyOwnerTargeting);
    }

    @Override
    public void start() {
        if (venerable.getOwner() != null && venerable.getOwner().getTarget() != null)
            venerable.setTarget(venerable.getOwner().getTarget());
        super.start();
    }
}
