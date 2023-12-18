package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner;

import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortal;
import com.eeeab.eeeabsmobs.sever.util.ModDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class OwnerDieGoal extends Goal {
    private final EntityImmortal immortal;
    private int downCount;

    public OwnerDieGoal(EntityImmortal immortal) {
        this.immortal = immortal;
        downCount = immortal.getRandom().nextInt(20);
    }

    @Override
    public boolean canUse() {
        Mob owner = immortal.getOwner();
        return owner != null && !owner.isAlive();
    }

    @Override
    public void tick() {
        if (downCount > 0) {
            downCount--;
        } else {
            immortal.hurt(DamageSource.indirectMagic(immortal, null), immortal.getHealth());
            //immortal.hurt(immortal.damageSources().indirectMagic(immortal, null), immortal.getHealth());
        }
    }
}
