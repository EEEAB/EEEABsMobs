package com.eeeab.eeeabsmobs.sever.entity.immortal.skeleton;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityImmortalSkeleton extends EntityAbsImmortalSkeleton {
    public EntityImmortalSkeleton(EntityType<? extends EntityImmortalSkeleton> type, Level level) {
        super(type, level);
    }

    @Override
    protected int getCareerId() {
        return CareerType.NONE.id;
    }
}
