package com.eeeab.eeeabsmobs.sever.entity;

import net.minecraft.world.entity.LivingEntity;

public interface IMobLevel {
    enum MobLevel {
        //特殊生物:例如NPC、召唤物
        NONE(0, 0F),
        NORMAL(10, 0.01F),
        HARD(50, 0.01F),
        ELITE(100, 0.02F),
        BOSS(300, 0.03F),
        EPIC_BOSS(500, 0.035F);

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
     * @return 获取生物级别
     */
    MobLevel getMobLevel();

    /**
     * @return 攻击时附带目标生命值百分比的伤害
     */
    default float getDamageAmountByTargetHealthPct(LivingEntity target) {
        return target.getMaxHealth() * getMobLevel().getDamagePct();
    }
}