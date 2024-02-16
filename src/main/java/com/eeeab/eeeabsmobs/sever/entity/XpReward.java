package com.eeeab.eeeabsmobs.sever.entity;

public enum XpReward {
    XP_REWARD_NONE(0),
    XP_REWARD_NORMAL(10),
    XP_REWARD_ELITE(50),
    XP_REWARD_BOSS(100);

    private final int xp;

    XpReward(int xp) {
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }
}