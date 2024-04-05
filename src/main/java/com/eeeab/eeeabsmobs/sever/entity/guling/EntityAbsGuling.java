package com.eeeab.eeeabsmobs.sever.entity.guling;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityAbsGuling extends EEEABMobLibrary {
    public EntityAbsGuling(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
    }
}
