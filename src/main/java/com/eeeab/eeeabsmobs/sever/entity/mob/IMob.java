package com.eeeab.eeeabsmobs.sever.entity.mob;

import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import net.minecraft.world.entity.LivingEntity;

public interface IMob {
    enum MobLevel {
        //特殊生物:例如NPC、召唤物
        NONE(0, 0F),
        EASY(10, 0.01F),
        NORMAL(50, 0.02F),
        ELITE(100, 0.025F),
        BOSS(300, 0.03F),
        LEGENDARY_BOSS(500, 0.04F);

        private final int xp;
        private final float damagePct;

        MobLevel(int xp, float damagePct) {
            this.xp = xp;
            this.damagePct = damagePct;
        }

        public int getXp() {
            return xp;
        }

        public float getDamagePct() {
            return damagePct;
        }
    }

    /**
     * @return 获取生物讨伐难度级别
     */
    MobLevel getMobLevel();

    /**
     * @return 攻击时附带目标生命值百分比的伤害
     */
    default float getDamageAmountByTargetHealthPct(LivingEntity target) {
        return target.getMaxHealth() * getMobLevel().getDamagePct();
    }

    /**
     * @return 检查实体是否处于眩晕状态
     */
    default boolean isStunned() {
        return false;
    }

    /**
     * @return 检查配置判断是否可以在破坏方块的时候掉落物品
     */
    default boolean checkCanDropItems() {
        return ModConfigHandler.COMMON.others.enableMobsCanBreakingBlockDropItem.get();
    }
}