package com.eeeab.animate.server.ai;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AnimationMeleeAI<T extends EEEABMobLibrary & EMAnimatedEntity> extends Goal {
    protected T attacker;
    protected Path path;
    protected final double speed;
    protected long lastCanUseCheck;
    protected int ticksUntilNextAttack;
    private final Supplier<Animation>[] animations;
    private final Predicate<T> customFlag;
    protected int attackInterval = 10;
    protected boolean followingTargetEvenIfNotSeen = false;

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
        this.attacker = attacker;
        this.speed = speed;
        this.customFlag = customFlag;
        this.animations = animations;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public AnimationMeleeAI<T> ignoreSight() {
        this.followingTargetEvenIfNotSeen = true;
        return this;
    }

    @Override
    public boolean canUse() {
        long gameTime = this.attacker.level().getGameTime();
        if (!this.customFlag.test(this.attacker)) {
            return false;
        } else if (gameTime - lastCanUseCheck < attackInterval) {
            return false;
        } else {
            lastCanUseCheck = gameTime;
            LivingEntity target = this.attacker.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else if (!this.attacker.canAttack(target)) {
                return false;
            } else {
                this.path = this.attacker.getNavigation().createPath(target, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.attacker.getMeleeAttackRangeSqr(target) >= this.attacker.distanceToSqr(target.getX(), target.getY(), target.getZ());
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (!this.customFlag.test(this.attacker)) {
            return false;
        } else if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.attacker.getNavigation().isDone();
        } else if (!this.attacker.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
        }
    }

    @Override
    public void start() {
        this.attacker.getNavigation().moveTo(this.path, this.speed);
        this.attacker.setAggressive(true);
    }

    @Override
    public void stop() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.attacker.setTarget(null);
        }
        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.attacker.getTarget();
        if (target != null) {
            this.attacker.getLookControl().setLookAt(target, 30F, 30F);
            double distance = this.attacker.getPerceivedTargetDistanceSquareForMeleeAttack(target);
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            if (this.ticksUntilNextAttack == 0 && this.attacker.getMeleeAttackRangeSqr(target) >= distance) {
                this.ticksUntilNextAttack = this.getAttackInterval();
                this.attacker.playAnimation(getAnimationByRandom());
            }
        }
    }

    protected int getAttackInterval() {
        return this.adjustedTickDelay(attackInterval);
    }

    //随机获得动画
    private Animation getAnimationByRandom() {
        if (animations == null || animations.length == 0) return this.attacker.getNoAnimation();
        return animations[this.attacker.getRandom().nextInt(animations.length)].get();
    }
}
