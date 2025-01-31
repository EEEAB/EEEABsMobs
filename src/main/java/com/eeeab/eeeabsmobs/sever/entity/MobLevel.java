package com.eeeab.eeeabsmobs.sever.entity;

public enum MobLevel {
    //特殊生物:例如NPC、召唤物
    NONE(0),
    NORMAL(10),
    HARD(50),
    ELITE(100),
    BOSS(300),
    EPIC_BOSS(500);

    private final int xp;

    MobLevel(int xp) {
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }
}