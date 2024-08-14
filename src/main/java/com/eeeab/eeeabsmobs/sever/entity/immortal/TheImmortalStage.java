package com.eeeab.eeeabsmobs.sever.entity.immortal;

public enum TheImmortalStage {
    STAGE1(0, 0F, 6F, 9F, (byte) 0),
    STAGE2(1, 250F, 8F, 11F, (byte) 1),
    STAGE3(2, 150F, 12F, 14F, (byte) 2);

    TheImmortalStage(int index, float addHealth, float addArmor, float addAttack, byte heldIndex) {
        this.index = index;
        this.addHealth = addHealth;
        this.addArmor = addArmor;
        this.addAttack = addAttack;
        this.heldIndex = heldIndex;
    }

    public final int index;
    public final float addHealth;
    public final float addArmor;
    public final float addAttack;
    public final byte heldIndex;

    public static TheImmortalStage byStage(int type) {
        for (TheImmortalStage value : values()) {
            if (value.index == type) {
                return value;
            }
        }
        return STAGE1;
    }
}