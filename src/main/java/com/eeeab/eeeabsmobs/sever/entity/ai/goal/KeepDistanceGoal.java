package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobEntity;
import com.eeeab.eeeabsmobs.sever.entity.NeedStopAiEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class KeepDistanceGoal<T extends EEEABMobEntity & NeedStopAiEntity> extends Goal {
    private final T mob;
    private final double speedModifier;
    private final float attackRadius;
    private final float moveBackSpeed;
    private boolean moveLeftOrRight;
    private boolean moveBack;
    private int timerTick = -1;

    public KeepDistanceGoal(T entity, double speedModifier, float attackRadius, float moveBackSpeedModifier) {
        this.mob = entity;
        this.speedModifier = speedModifier;//移动目标速度
        this.attackRadius = attackRadius;//攻击范围
        if (moveBackSpeedModifier < 0) {
            moveBackSpeedModifier = 0.5F;
        }
        this.moveBackSpeed = moveBackSpeedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() != null && this.mob.noConflictingTasks();
    }


    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        this.mob.getMoveControl().strafe(0F, 0F);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            double distance = this.mob.targetDistance;
            if (!(distance > (double) this.attackRadius)) {//当实体距离小于攻击半径
                this.mob.getNavigation().stop();
                ++this.timerTick;
            } else {
                this.mob.getNavigation().moveTo(target, this.speedModifier);
                this.timerTick = -1;
            }

            if (this.timerTick >= 20) {//当实体距离小于攻击半径将会每一秒执行一次
                if ((double) this.mob.getRandom().nextFloat() < 0.25D) {
                    this.moveLeftOrRight = !this.moveLeftOrRight;
                }
                this.timerTick = 0;
            }

            if (this.timerTick > -1) {
                if (distance > (double) (this.attackRadius * 0.6F)) {
                    this.moveBack = false;
                } else if (distance < (double) (this.attackRadius * 0.4F)) {
                    this.moveBack = true;
                }

                this.mob.getMoveControl().strafe(this.moveBack ? -1F * moveBackSpeed : 0.5F, this.moveLeftOrRight ? 0.5F : -0.5F);
                this.mob.lookAt(target, 90.0F, 90.0F);
            }
        }
    }
}

