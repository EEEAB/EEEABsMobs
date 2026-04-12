package com.eeeab.animate.server.ai;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;

import java.util.Arrays;
import java.util.EnumSet;

public class AnimationGroupAI<T extends EEEABMobLibrary & AnimatedEntity> extends AnimationAI<T> {
    protected final Animation[] animations;

    public AnimationGroupAI(T entity, Animation... animations) {
        super(entity);
        this.animations = animations;
    }

    public AnimationGroupAI(T entity, boolean canStopGoal, Animation... animations) {
        super(entity, canStopGoal);
        this.animations = animations;
    }

    public AnimationGroupAI(T entity, boolean canStopGoal, boolean hurtInterruptsAnimation, Animation... animations) {
        super(entity, canStopGoal, hurtInterruptsAnimation);
        this.animations = animations;
    }

    public AnimationGroupAI(T entity, EnumSet<Flag> interruptFlagTypes, Animation... animations) {
        super(entity, false, false);
        this.animations = animations;
        this.setFlags(interruptFlagTypes);
    }

    @Override
    protected boolean test(Animation animation) {
        return Arrays.stream(this.animations).anyMatch(definition -> animation == definition);
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
