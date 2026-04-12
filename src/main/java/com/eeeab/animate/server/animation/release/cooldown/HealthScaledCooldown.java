package com.eeeab.animate.server.animation.release.cooldown;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/**
 * 基于生命值百分比的冷却时间范围生成器
 *
 * @author EEEAB
 */
public class HealthScaledCooldown implements CooldownGenerator {
    private final int base;
    private final int deviation;
    private final int reduction;
    private final float threshold;
    private final boolean diffBaseDynaCD;

    public HealthScaledCooldown(int base, int deviation, int reduction, float threshold) {
        this(base, deviation, reduction, threshold, false);
    }

    public HealthScaledCooldown(int base, int deviation, int reduction, float threshold, boolean diffBaseDynaCD) {
        this.base = base;
        this.deviation = deviation;
        this.reduction = reduction;
        this.threshold = Mth.clamp(threshold, 0F, 1F);
        this.diffBaseDynaCD = diffBaseDynaCD;
        if (reduction >= base) {
            throw new IllegalArgumentException("reduction must be < base");
        }
    }

    @Override
    public int generate(LivingEntity entity) {
        int baseCD = getCoolingTimerUtil(entity, base, base - reduction, threshold);
        int cd = Mth.randomBetweenInclusive(entity.getRandom(), baseCD - deviation, baseCD + deviation);
        if (diffBaseDynaCD) cd *= getDifficultyMultiplier(entity.level().getDifficulty());
        return cd;
    }

    private static int getCoolingTimerUtil(LivingEntity entity, int maxCooling, int minCooling, float healthPercentage) {
        float maximumCoolingPercentage = 1 - healthPercentage;
        float ratio = 1 - entity.getHealth() / entity.getMaxHealth();
        if (ratio > maximumCoolingPercentage) {
            ratio = maximumCoolingPercentage;
        }
        return (int) (maxCooling - (ratio / maximumCoolingPercentage) * (maxCooling - minCooling));
    }
}