package com.eeeab.animate.server.ai;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Supplier;

public class AnimationGroupAI<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationAI<T> {
    protected final Supplier<Animation>[] animations;

    @SafeVarargs
    public AnimationGroupAI(T entity, @NotNull Supplier<Animation>... animations) {
        super(entity);
        this.animations = animations;
    }

    @SafeVarargs
    public AnimationGroupAI(T entity, boolean canStopGoal, @NotNull Supplier<Animation>... animations) {
        super(entity, canStopGoal);
        this.animations = animations;
    }

    @SafeVarargs
    public AnimationGroupAI(T entity, boolean canStopGoal, boolean hurtInterruptsAnimation, @NotNull Supplier<Animation>... animations) {
        super(entity, canStopGoal, hurtInterruptsAnimation);
        this.animations = animations;
    }

    @Override
    protected boolean test(Animation animation) {
        return Arrays.stream(this.animations).anyMatch(animationSupplier -> animation == animationSupplier.get());
    }

    protected void nextAnimation(Animation now, Animation next) {
        this.nextAnimation(now, next, now.getDuration() - 1);
    }

    protected void nextAnimation(Animation now, Animation next, int lastTick) {
        this.nextAnimation(now, next, this.entity.getAnimationTick() >= lastTick);
    }

    protected void nextAnimation(Animation now, Animation next, boolean flag) {
        this.nextAnimation(now, flag, next);
    }

    protected boolean nextAnimation(Animation now, boolean flag, Animation... nextAnimations) {
        if (nextAnimations == null || nextAnimations.length == 0) return false;
        if (this.entity.getAnimation() == now && flag) {
            this.entity.playAnimation(nextAnimations[this.entity.getRandom().nextInt(nextAnimations.length)]);
            return true;
        }
        return false;
    }
}
