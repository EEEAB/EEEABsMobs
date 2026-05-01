package com.eeeab.eeeabsmobs.sever.entity.mob;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface SummoningEntity<T extends LivingEntity> {
    TargetingConditions DEFAULT = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    @Nullable
    T getOwner();

    void setOwner(@Nullable T owner);

    UUID getOwnerUUID();

    void setOwnerUUID(UUID uuid);

    boolean isSummon();

    default boolean belongsToMob() {
        return true;
    }

    default List<? extends Mob> getNearByEntitiesByClass(Class<? extends Mob> ownerClass, Level level, T entity, double x, double y, double z, double radius) {
        return level.getEntitiesOfClass(ownerClass, entity.getBoundingBox().inflate(x, y, z), targetEntity -> targetEntity != entity &&
                entity.distanceTo(targetEntity) <= radius + targetEntity.getBbWidth() / 2f && targetEntity.getY() <= entity.getY() + y);
    }

    default void shiftHatred(T entity, double radius) {
        entity.level().getNearbyEntities(Mob.class, DEFAULT, entity, entity.getBoundingBox().inflate(radius))
                .stream().filter(mob -> mob != entity && mob != getOwner() && mob.isAlive() && mob.getTarget() == entity && !entity.isAlliedTo(mob))
                .forEach(mob -> {
                    if (mob instanceof SummoningEntity<?> summoning) {
                        if (summoning.getOwner() == getOwner()) return;
                    }
                    mob.setTarget(getOwner());
                    Brain<?> mobBrain = mob.getBrain();
                    if (mobBrain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
                        mobBrain.setMemory(MemoryModuleType.ATTACK_TARGET, this.getOwner());
                    }
                });
    }
}
