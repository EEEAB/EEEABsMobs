package com.eeeab.eeeabsmobs.sever.entity.corpse;

import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityCorpseVillager extends EntityCorpse implements IEntity {
    public EntityCorpseVillager(EntityType<? extends EntityAbsCorpse> type, Level level) {
        super(type, level);
    }
}
