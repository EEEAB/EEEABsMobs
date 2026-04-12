package com.eeeab.animate.server.ai;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;

import java.util.EnumSet;

public class AnimationSimpleAI<T extends EEEABMobLibrary & AnimatedEntity> extends AnimationAI<T> {
    protected final Animation animation;

    public AnimationSimpleAI(T entity, Animation animationSupplier) {
        super(entity);
        this.animation = animationSupplier;
    }

    public AnimationSimpleAI(T entity, Animation animationSupplier, boolean canStopGoal) {
        super(entity, canStopGoal);
        this.animation = animationSupplier;
    }

    public AnimationSimpleAI(T entity, Animation animationSupplier, boolean canStopGoal, boolean hurtInterruptsAnimation) {
        super(entity, canStopGoal, hurtInterruptsAnimation);
        this.animation = animationSupplier;
    }

    //指定Flag构造器
    public AnimationSimpleAI(T entity, Animation animationSupplier, EnumSet<Flag> interruptFlagTypes) {
        super(entity);
        this.animation = animationSupplier;
        setFlags(interruptFlagTypes);
    }

    //获取实体当前动画与传入动画进行对比
    @Override
    protected boolean test(Animation animation) {
        return animation == this.animation;
    }
}
