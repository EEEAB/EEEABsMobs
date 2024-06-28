package com.eeeab.eeeabsmobs.sever.entity;

public enum XpReward {
    XP_REWARD_NONE(0),
    XP_REWARD_NORMAL(10),
    XP_REWARD_HARD(50),
    XP_REWARD_ELITE(100),
    XP_REWARD_BOSS(200),
    XP_REWARD_EPIC_BOSS(500);

    private final int xp;

    XpReward(int xp) {
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }
}