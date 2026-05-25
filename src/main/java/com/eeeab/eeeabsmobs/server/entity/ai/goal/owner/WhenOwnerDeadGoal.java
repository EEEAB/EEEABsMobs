package com.eeeab.eeeabsmobs.server.entity.ai.goal.owner;

import com.eeeab.eeeabsmobs.server.entity.mob.SummoningEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class WhenOwnerDeadGoal<T extends LivingEntity & SummoningEntity<T>> extends Goal {
    private final T venerable;
    private int downCount;

    public WhenOwnerDeadGoal(T venerable) {
        this.venerable = venerable;
        downCount = venerable.getRandom().nextInt(20);
    }

    @Override
    public boolean canUse() {
        T owner = venerable.getOwner();
        return owner != null && !owner.isAlive();
    }

    @Override
    public void tick() {
        if (downCount > 0) {
            downCount--;
        } else {
            venerable.hurt(venerable.damageSources().indirectMagic(venerable, null), venerable.getHealth());
        }
    }
}
