package com.eeeab.eeeabsmobs.sever.block.properties;

import net.minecraft.util.StringRepresentable;

public enum SGACharacter implements StringRepresentable {
    A, B, C, D, E, F, G, H, I, J, K, L, M,
    N, O, P, Q, R, S, T, U, V, W, X, Y, Z;

    @Override
    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}