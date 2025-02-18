package com.eeeab.eeeabsmobs.sever.entity.immortal.skeleton;

import com.eeeab.animate.server.ai.AnimationMeleeAI;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityImmortalWarrior extends EntityAbsImmortalSkeleton {
    public EntityImmortalWarrior(EntityType<? extends EntityImmortalWarrior> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(4, new AnimationMeleeAI<>(this, 1D, 10 + this.random.nextInt(10), e -> e.active, () -> meleeAnimation1, () -> meleeAnimation2));
        super.registerCustomGoals();
    }

    @Override
    protected int getCareerId() {
        return CareerType.WARRIOR.id;
    }
}
