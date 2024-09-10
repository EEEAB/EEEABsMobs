package com.eeeab.eeeabsmobs.sever.entity;

import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public interface IBoss extends IEntity {

    /**
     * @return 检查玩家攻击是否无效
     */
    default boolean checkPlayerAttackLegality(Player player, Mob boss, double height) {
        if (player.isSpectator() || player.isCreative()) return false;
        boolean flag = false;
        if (boss.getSensing().hasLineOfSight(player)) {
            double pY = player.getY();
            double bY = boss.getY();
            flag = player.isFallFlying() || Math.abs(pY - bY) <= height;
        }
        if (!flag) boss.playSound(SoundInit.UNDAMAGED.get(), 0.4F, 2F);
        return !flag;
    }
}
