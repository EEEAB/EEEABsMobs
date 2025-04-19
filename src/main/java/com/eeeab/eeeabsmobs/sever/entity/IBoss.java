package com.eeeab.eeeabsmobs.sever.entity;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;

public interface IBoss extends IEntity {

    /**
     * @return 检查boss是否处于挑战模式
     */
    default boolean isChallengeMode() {
        return false;
    }

    /**
     * @return 检查配置判断是否可以在boss破坏方块的时候掉落物品
     */
    default boolean checkCanDropItems() {
        return EMConfigHandler.COMMON.OTHER.enableBossCanBreakingBlockDropItem.get();
    }
}
