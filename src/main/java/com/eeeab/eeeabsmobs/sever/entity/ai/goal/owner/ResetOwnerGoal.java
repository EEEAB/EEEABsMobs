package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner;

import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortal;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class ResetOwnerGoal<OWNER extends EntityImmortal> extends Goal {
    private final EntityImmortal immortal;
    private final Class<OWNER> ownerClass;
    private final double findRadius;

    public ResetOwnerGoal(EntityImmortal immortal, Class<OWNER> ownerClass, double findRadius) {
        this.immortal = immortal;
        this.ownerClass = ownerClass;
        this.findRadius = findRadius;
    }

    @Override
    public boolean canUse() {
        return immortal.getOwner() == null && immortal.getOwnerUUID() != null;
    }

    @Override
    public void start() {
        List<OWNER> entities = immortal.getNearByEntities(ownerClass, findRadius, findRadius, findRadius, findRadius);
        for (OWNER entity : entities) {
            if (immortal.getOwnerUUID().equals(entity.getUUID())) {
                this.immortal.setOwner(entity);
            }
        }
    }
}
