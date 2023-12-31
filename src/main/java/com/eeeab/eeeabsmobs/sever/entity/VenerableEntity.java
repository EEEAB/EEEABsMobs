package com.eeeab.eeeabsmobs.sever.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface VenerableEntity<T extends Mob> {

    @Nullable
    T getOwner();

    void setOwner(@Nullable T owner);

    UUID getOwnerUUID();

    void setOwnerUUID(UUID uuid);

    boolean isSummon();

    default List<? extends Mob> getNearByEntitiesByClass(Class<? extends Mob> ownerClass, Level level, T entity, double x, double y, double z, double radius) {
        return level.getEntitiesOfClass(ownerClass, entity.getBoundingBox().inflate(x, y, z), targetEntity -> targetEntity != entity &&
                entity.distanceTo(targetEntity) <= radius + targetEntity.getBbWidth() / 2f && targetEntity.getY() <= entity.getY() + y);
    }
}
