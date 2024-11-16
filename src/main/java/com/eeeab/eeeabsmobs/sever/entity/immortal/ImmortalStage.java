package com.eeeab.eeeabsmobs.sever.entity.immortal;

import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum ImmortalStage {
    STAGE1((byte) 0, 0F, 0F, 0F, false),
    STAGE2((byte) 1, -100F, -2F, 6F, true);

    ImmortalStage(byte id, float addHealth, float addArmor, float addAttack, boolean holdFlag) {
        this.id = id;
        this.addHealth = addHealth;
        this.addArmor = addArmor;
        this.addAttack = addAttack;
        this.holdKatana = holdFlag;
    }

    public final byte id;
    public final float addHealth;
    public final float addArmor;
    public final float addAttack;
    public final boolean holdKatana;
    private static final IntFunction<ImmortalStage> BY_ID = ByIdMap.sparse(c -> c.id, values(), STAGE1);

    public static ImmortalStage byStage(int type) {
        return BY_ID.apply(type);
    }
}