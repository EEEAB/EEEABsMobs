package com.eeeab.animate.server.animation.release.cooldown;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/**
 * 冷却时间范围生成器
 *
 * @author EEEAB
 */
public class FixedRangeCooldown implements CooldownGenerator {
    private final int base;
    private final int deviation;
    private final boolean diffBaseDynaCD;

    public FixedRangeCooldown(int base, int deviation) {
        this(base, deviation, false);
    }

    public FixedRangeCooldown(int base, int deviation, boolean diffBaseDynaCD) {
        this.base = base;
        this.deviation = deviation;
        this.diffBaseDynaCD = diffBaseDynaCD;
        if (deviation < 0) {
            throw new IllegalArgumentException("deviation cannot be negative");
        }
    }

    @Override
    public int generate(LivingEntity entity) {
        int cd = Mth.randomBetweenInclusive(entity.getRandom(), base - deviation, base + deviation);
        if (diffBaseDynaCD) cd *= getDifficultyMultiplier(entity.level().getDifficulty());
        return cd;
    }
}