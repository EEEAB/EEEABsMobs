package com.eeeab.eeeabsmobs.server.entity.mob;

import com.eeeab.eeeabsmobs.server.handler.ModConfigHandler;
import net.minecraft.world.entity.LivingEntity;

public interface IMob {
    enum MobLevel {
        //特殊生物:例如NPC、召唤物
        NONE(0, 0F, false, false),
        EASY(10, 0.01F, false, false),
        NORMAL(50, 0.02F, false, false),
        HARD(100, 0.03F, false, true),
        EXPERT(300, 0.04F, true, true),
        BRUTAL(500, 0.05F, true, true),
        NIGHTMARE(1000, 0.1F, true, true);

        private final int xp;
        private final float damagePct;
        private final boolean boss;
        private final boolean destroyBlock;

        MobLevel(int xp, float damagePct, boolean boss, boolean destroyBlock) {
            this.xp = xp;
            this.damagePct = damagePct;
            this.boss = boss;
            this.destroyBlock = destroyBlock;
        }

        public int getXp() {
            return xp;
        }

        public float getDamagePct() {
            return damagePct;
        }

        public boolean canDestroyBlock() {
            return destroyBlock;
        }

        public boolean isBoss() {
            return boss;
        }
    }

    /**
     * @return 获取生物讨伐难度级别
     */
    MobLevel getMobLevel();

    /**
     * @return 讨伐难度是否是首领级
     */
    default boolean isBossLevel() {
        return getMobLevel().isBoss();
    }

    /**
     * @return 是否允许在受击时破坏方块
     */
    default boolean canDestroyBlocksWhenHurt() {
        return getMobLevel().canDestroyBlock();
    }

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
     * @return 检查配置判断是否允许模组生物破坏性行为
     */
    default boolean mobGriefing() {
        return ModConfigHandler.COMMON.others.enableMobGriefing.get();
    }

    /**
     * @return 检查配置判断是否可以在破坏方块时掉落物品
     */
    default boolean canItemDropsWhenBreakBlocks() {
        return ModConfigHandler.COMMON.others.enableItemDropsWhenBreakBlocks.get();
    }
}