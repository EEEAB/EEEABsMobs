package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner;

import com.eeeab.eeeabsmobs.sever.entity.SummoningEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class WhenOwnerDeadGoal<T extends Mob & SummoningEntity<T>> extends Goal {
    private final T venerable;
    private int downCount;

    public WhenOwnerDeadGoal(T venerable) {
        this.venerable = venerable;
        downCount = venerable.getRandom().nextInt(20);
    }

    @Override
    public boolean canUse() {
        Mob owner = venerable.getOwner();
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
