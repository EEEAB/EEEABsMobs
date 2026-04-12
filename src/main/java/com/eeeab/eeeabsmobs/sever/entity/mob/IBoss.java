package com.eeeab.eeeabsmobs.sever.entity.mob;

public interface IBoss extends IMob {

    /**
     * @return 检查boss是否处于挑战模式
     */
    default boolean isChallengeMode() {
        return false;
    }
}
