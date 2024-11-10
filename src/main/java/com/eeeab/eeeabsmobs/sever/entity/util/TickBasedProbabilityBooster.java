package com.eeeab.eeeabsmobs.sever.entity.util;

/**
 * 随游戏刻增长概率器
 *
 * @author EEEAB
 */
public class TickBasedProbabilityBooster {
    /**
     * 基础概率
     */
    private final float baseProbability;
    /**
     * 每tick增长的概率值
     */
    private final float probabilityGrowthPerTick;
    /**
     * 概率增长的上限
     */
    private final float maxProbability;
    /**
     * 当前概率
     */
    private float currentProbability;

    public TickBasedProbabilityBooster(float base, float growth, float max) {
        this.currentProbability = this.baseProbability = base;
        this.probabilityGrowthPerTick = growth;
        this.maxProbability = max;
    }

    /**
     * 随tick增长概率
     */
    public TickBasedProbabilityBooster onTick() {
        if (currentProbability < maxProbability) {
            currentProbability += probabilityGrowthPerTick;
            if (currentProbability > maxProbability) {
                currentProbability = maxProbability;
            }
        }
        return this;
    }

    /**
     * @return 当前概率
     */
    public float getProbability() {
        return currentProbability;
    }

    /**
     * 重置概率
     */
    public void resetProbability() {
        currentProbability = baseProbability;
    }
}