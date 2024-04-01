package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner;

import com.eeeab.eeeabsmobs.sever.entity.VenerableEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class OwnerProtectGoal<T extends Mob & VenerableEntity<T>> extends Goal {
    private final T venerable;

    public OwnerProtectGoal(T venerable) {
        this.venerable = venerable;
    }

    @Override
    public boolean canUse() {
        if (this.venerable.getOwner() != null) {
            T owner = this.venerable.getOwner();
            if (!owner.isAlive()) return false;
            LivingEntity target = owner.getTarget();
            if (target instanceof Mob mob) {
                return mob.getTarget() == owner;
            }
        }
        return false;
    }

    @Override
    public void start() {
        T owner = this.venerable.getOwner();
        if (owner != null) {
            LivingEntity target = owner.getTarget();
            if (target instanceof Mob mob) {
                mob.setTarget(this.venerable);
                if (mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
                    mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, this.venerable);
                }
            }
        }
    }
}
