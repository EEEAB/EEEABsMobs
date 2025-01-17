package com.eeeab.eeeabsmobs.sever.entity;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public interface IBoss extends IEntity {
    String STRING_ILLEGALITY_COUNT = "illegalityCount";

    int getIllegalityCount();

    void setIllegalityCount(int count);

    default int getMaxIllegalityCount() {
        return 10;
    }

    /**
     * 当非法攻击次数达到设定的最大值时 应让Boss脱离战斗并返回出生点
     *
     * @return 是否可以脱战
     */
    default boolean outOfCombatFlag() {
        return getIllegalityCount() >= getMaxIllegalityCount();
    }

    /**
     * 非法攻击次数统计
     *
     * @param flag 条件为ture则增长次数反之减少
     */
    default void changeIllegalityCount(boolean flag) {
        setIllegalityCount(Mth.clamp(getIllegalityCount() + (flag ? 1 : -1), 0, getMaxIllegalityCount()));
    }

    default void addBossSaveData(CompoundTag nbt) {
        nbt.putInt(STRING_ILLEGALITY_COUNT, this.getIllegalityCount());
    }

    default void readBossSaveData(CompoundTag nbt) {
        setIllegalityCount(nbt.getInt(STRING_ILLEGALITY_COUNT));
    }

    /**
     * 检查玩家攻击是否合法
     */
    default void checkPlayerAttackLegality(Player player, Mob boss, double height) {
        if (player.isSpectator() || player.isCreative()) return;
        boolean flag = false;
        if (boss.getSensing().hasLineOfSight(player)) {
            double pY = player.getY();
            double bY = boss.getY();
            flag = player.isFallFlying() /* 玩家在使用鞘翅飞行时不做Y轴判断 */ || Math.abs(pY - bY) <= height;
        }
        if (!flag) boss.playSound(SoundInit.UNDAMAGED.get(), 1F, 2F);
        changeIllegalityCount(!flag);
    }

    /**
     * @return 检查boss通用配置判断是否可以在破坏方块的时候掉落物品
     */
    default boolean checkCanDropItems() {
        return EMConfigHandler.COMMON.OTHER.enableBossCanBreakingBlockDropItem.get();
    }
}
