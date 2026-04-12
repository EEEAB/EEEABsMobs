package com.eeeab.animate.server.animation.release;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import net.minecraft.world.entity.Mob;

/**
 * 条件工厂 - 提供常用的动画释放条件
 *
 * @author EEEAB
 */
public class ConditionFactory {
    /**
     * 距离条件
     */
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> distanceRange(double min, double max) {
        return distanceRange(min, max, max);
    }

    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> distanceRange(double min, double max, double forecastMax) {
        return (entity, target) -> {
            if (target == null) return false;
            double maxRange = max;
            if (ModEntityUtils.checkTargetComingCloser(entity, target)) {
                maxRange = forecastMax;
            }
            float width = target.getBbWidth() / 2;
            double distance = Math.max(entity.distanceToSqr(target) - (width * width), 0);
            return distance >= min * min && distance <= maxRange * maxRange;
        };
    }

    /**
     * 混合距离条件
     */
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> hybridDistanceRange(double height, double minDist, double maxDist) {
        return (entity, target) -> {
            if (target == null) return false;
            float width = target.getBbWidth() / 2;
            double dx = entity.getX() - target.getX();
            double dz = entity.getZ() - target.getZ();
            double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
            double parallelDistance = Math.max(horizontalDistance - width, 0);
            double heightDiff = Math.abs(entity.getY() - target.getY());
            if (parallelDistance >= heightDiff) {
                return parallelDistance >= minDist && parallelDistance <= maxDist;
            } else {
                return heightDiff <= height;
            }
        };
    }

    /**
     * 高度差条件
     */
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> heightDiff(double max) {
        return (entity, target) -> {
            if (target == null) return false;
            return Math.abs(entity.getY() - target.getY()) <= max;
        };
    }

    /**
     * 视线条件
     */
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> hasLineOfSight() {
        return (entity, target) -> target != null && entity.getSensing().hasLineOfSight(target);
    }

    /**
     * 角度条件
     */
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> angleRange(float min, float max) {
        return (entity, target) -> {
            if (target == null) return false;
            float angle = ModEntityUtils.getTargetRelativeAngle(entity, target);
            return angle >= min && angle <= max;
        };
    }

    /**
     * 血量百分比条件
     */
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> healthBelow(float percentage) {
        return (entity, target) -> {
            float healthPct = entity.getHealth() / entity.getMaxHealth();
            return healthPct < percentage;
        };
    }

    /**
     * 随机概率条件
     */
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> randomChance(float chance) {
        return (entity, target) -> entity.getRandom().nextFloat() < chance;
    }

    /**
     * 根据生命值降低线性增加随机概率条件
     *
     * @param baseProb   基础触发概率[0-1]
     * @param percentage 概率增量与触发阈值(生命值低于1-percentage增加概率)
     */
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> randomChanceOnLowHealth(float baseProb, float percentage) {
        return (entity, target) -> {
            float healthPct = entity.getHealth() / entity.getMaxHealth();
            float prob = baseProb;
            if (healthPct <= 1 - percentage) prob += percentage;
            else prob += percentage * (1 - (healthPct - (1 - percentage)) / percentage);
            return entity.getRandom().nextFloat() < prob;
        };
    }

    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> randomChanceOnHighHealth(float fullHealthProb, float lowHealthProb) {
        float baseProb = 1 - fullHealthProb;
        float percentage = fullHealthProb - lowHealthProb;
        return (entity, target) -> !randomChanceOnLowHealth(baseProb, percentage).test(entity, target);
    }

    /**
     * 复合条件 - AND
     */
    @SafeVarargs
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> and(AnimationCondition<T>... conditions) {
        return (entity, target) -> {
            for (AnimationCondition<T> condition : conditions) {
                if (!condition.test(entity, target)) return false;
            }
            return true;
        };
    }

    /**
     * 复合条件 - OR
     */
    @SafeVarargs
    public static <T extends Mob & AnimatedEntity> AnimationCondition<T> or(AnimationCondition<T>... conditions) {
        return (entity, target) -> {
            for (AnimationCondition<T> condition : conditions) {
                if (condition.test(entity, target)) return true;
            }
            return false;
        };
    }
}