package com.eeeab.animate.server.ai;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.function.Supplier;

public class AnimationMeleePlusAI<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationMeleeAI<T> {
    private int attackIndex;
    private int delayCounter;
    private int delay = 5;

    @SafeVarargs
    public AnimationMeleePlusAI(T attacker, double speed, int attackInterval, Supplier<Animation>... animations) {
        super(attacker, speed, attackInterval, e -> e.active, animations);
    }

    @SafeVarargs
    public AnimationMeleePlusAI(T attacker, double speed, int attackInterval, int delay, Supplier<Animation>... animations) {
        this(attacker, speed, attackInterval, animations);
        if (delay < 1) {
            delay = 1;
        }
        this.delay = delay;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.attacker.getTarget();
        return this.customFlag.test(this.attacker) && target != null && target.isAlive() && this.attacker.canAttack(target);
    }

    @Override
    public void tick() {
        LivingEntity target = this.attacker.getTarget();
        if (target != null) {
            this.attacker.getLookControl().setLookAt(target, 30.0F, 30.0F);
            double distSqr = this.attacker.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
            if (animations != null && animations.length != 0) {
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                if (this.ticksUntilNextAttack == 0 && this.attacker.getMeleeAttackRangeSqr(target) >= distSqr) {
                    this.ticksUntilNextAttack = this.getAttackInterval();
                    this.attacker.playAnimation(this.getAnimationByPolling());
                    this.delayCounter += 5;
                }
            }
            if (--this.delayCounter <= 0) {
                distSqr = this.attacker.distanceToSqr(target.getX(), target.getBoundingBox().minY, target.getZ());
                this.delayCounter = this.delay + this.attacker.getRandom().nextInt(this.delay) + 1;
                if (distSqr > Math.pow(this.attacker.getAttributeValue(Attributes.FOLLOW_RANGE), 2.0)) {
                    if (this.attacker.getNavigation().isDone() && !this.attacker.getNavigation().moveTo(target, 1.0)) {
                        this.delayCounter += this.delay;
                    }
                } else {
                    this.attacker.getNavigation().moveTo(target, this.speed);
                }
            }
        }
    }

    //轮询获得动画
    private Animation getAnimationByPolling() {
        if (attackIndex < 0) attackIndex = 0;
        return animations[attackIndex++ % animations.length].get();
    }
}
