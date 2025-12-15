package com.eeeab.eeeabsmobs.sever.entity.mob.immortal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityImmortalSkeleton extends EntityAbsImmortalSkeleton {
    public EntityImmortalSkeleton(EntityType<? extends EntityImmortalSkeleton> type, Level level) {
        super(type, level);
    }

    @Override
    protected int getClassId() {
        return ClassType.NONE.id;
    }
}
