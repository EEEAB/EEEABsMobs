package com.eeeab.animate.server.animation.release;

import com.eeeab.animate.server.animation.AnimatedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/**
 * 动画释放条件
 *
 * @author EEEAB
 */
@FunctionalInterface
public interface AnimationCondition<T extends Mob & AnimatedEntity> {
    boolean test(T entity, LivingEntity target);
}