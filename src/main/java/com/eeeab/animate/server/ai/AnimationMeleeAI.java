package com.eeeab.animate.server.ai;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class AnimationMeleeAI<T extends EEEABMobLibrary & EMAnimatedEntity> extends MeleeAttackGoal {
    protected T attacker;
    protected final double speed;
    protected int ticksUntilNextAttack;
    protected final Supplier<Animation>[] animations;
    protected final Predicate<T> customFlag;
    protected int attackInterval = 10;

    @SafeVarargs
    public AnimationMeleeAI(T attacker, double speed, Supplier<Animation>... animations) {
        this(attacker, speed, e -> e.active, animations);
    }

    @SafeVarargs
    public AnimationMeleeAI(T attacker, double speed, int attackInterval, Supplier<Animation>... animations) {
        this(attacker, speed, e -> e.active, animations);
        this.attackInterval = attackInterval;
    }

    @SafeVarargs
    public AnimationMeleeAI(T attacker, double speed, int attackInterval, Predicate<T> customFlag, Supplier<Animation>... animations) {
        this(attacker, speed, customFlag, animations);
        this.attackInterval = attackInterval;
    }

    @SafeVarargs
    public AnimationMeleeAI(T attacker, double speed, Predicate<T> customFlag, Supplier<Animation>... animations) {
        super(attacker, speed, true);
        this.attacker = attacker;
        this.speed = speed;
        this.customFlag = customFlag;
        this.animations = animations;
    }

    @Override
    public boolean canUse() {
        if (!this.customFlag.test(this.attacker)) {
            return false;
        } else {
            return super.canUse();
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.customFlag.test(this.attacker)) {
            return false;
        } else {
            return super.canContinueToUse();
        }
    }

    @Override
    public void tick() {
        if (this.attacker.getTarget() != null) {
            if (this.attacker.isNoAnimation()) this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
        super.tick();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity target, double distToEnemySqr) {
        if (this.attacker.isNoAnimation() && this.ticksUntilNextAttack == 0 && this.attacker.getMeleeAttackRangeSqr(target) >= distToEnemySqr) {
            this.ticksUntilNextAttack = this.getAttackInterval();
            this.attacker.playAnimation(getAnimationByRandom());
        }
    }

    @Override
    protected int getAttackInterval() {
        return this.adjustedTickDelay(attackInterval);
    }

    //随机获得动画
    private Animation getAnimationByRandom() {
        if (animations == null || animations.length == 0) return this.attacker.getNoAnimation();
        return animations[this.attacker.getRandom().nextInt(animations.length)].get();
    }
}
