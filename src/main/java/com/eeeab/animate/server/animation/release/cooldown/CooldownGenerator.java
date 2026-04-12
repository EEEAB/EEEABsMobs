package com.eeeab.animate.server.animation.release.cooldown;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;

/**
 * 冷却时间生成器
 *
 * @author EEEAB
 */
@FunctionalInterface
public interface CooldownGenerator {
    int generate(LivingEntity entity);

    /**
     * 基于当前游戏难度动态调整冷却时间
     */
    default double getDifficultyMultiplier(Difficulty difficulty) {
        return switch (difficulty) {
            case HARD -> 1;
            case EASY, PEACEFUL -> 1.3;
            default -> 1.15;
        };
    }
}